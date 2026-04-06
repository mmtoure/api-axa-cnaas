package sn.axa.apiaxacnaas.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sn.axa.apiaxacnaas.dto.GroupDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.*;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.GroupMapper;
import sn.axa.apiaxacnaas.mappers.InsuredMapper;
import sn.axa.apiaxacnaas.repositories.GroupRepository;
import sn.axa.apiaxacnaas.repositories.InsuredRepository;
import sn.axa.apiaxacnaas.util.GroupStatus;
import sn.axa.apiaxacnaas.util.InsuredStatus;
import sn.axa.apiaxacnaas.util.RoleEnum;
import sn.axa.apiaxacnaas.util.SubscriptionTypeEnum;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UserService userService;
    private final ContractService contractService;
    private final InsuredRepository insuredRepository;
    private final InsuredMapper insuredMapper;
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private final TemplateEngine templateEngine;
    @Value("${app.pdf.storage-path}")
    private String storagePath;

    public GroupDTO subscribeGroup(GroupDTO groupDTO, MultipartFile file ){
        User currentUser = userService.getCurrentUser();
        Partner currentPartner = currentUser.getPartner();
        Group group = groupMapper.toEntity(groupDTO);
        group.setCreatedBy(currentUser);
        group.setPartner(currentPartner);
        group.setSubscriptionDate(LocalDate.now());
        group.setStatus(GroupStatus.ACTIF);
        Set<Insured> insureds = parseExcel(file, group);
        Group savedGroup = groupRepository.save(group);
        if(!insureds.isEmpty()){
            insureds.forEach(insured -> {
                insured.setGroup(savedGroup);
                insured.setStatus(InsuredStatus.ACTIF);
                insured.setCreatedBy(currentUser);
                insured.setSubscriptionType(SubscriptionTypeEnum.GROUPEMENT);
                insured.setSubscriptionDate(LocalDate.now());
                insured.setPartner(currentPartner);
                insured.setSubscriptionDate(LocalDate.now());
                insuredRepository.save(insured);
                contractService.createContract(insured);
            });
        }
        return groupMapper.toDTO(savedGroup);
    }
    public GroupDTO createGroup(GroupDTO groupDTO){
        User currentUser = userService.getCurrentUser();
        Partner currentPartner = currentUser.getPartner();
        Group group = groupMapper.toEntity(groupDTO);
        group.setUser(currentUser);
        group.setCreatedBy(currentUser);
        group.setSubscriptionDate(LocalDate.now());
        group.setStatus(GroupStatus.ACTIF);
        Group savedGroup = groupRepository.save(group);

        if(group.getInsureds()!=null){
            group.getInsureds().forEach(insured -> {
                insured.setGroup(savedGroup);

                insured.setUser(currentUser);
                insured.setStatus(InsuredStatus.ACTIF);
                insured.setCreatedBy(currentUser);
                insured.setSubscriptionType(SubscriptionTypeEnum.GROUPEMENT);
                insured.setSubscriptionDate(LocalDate.now());
                insuredRepository.save(insured);
                contractService.createContract(insured);
            });
        }
        return groupMapper.toDTO(savedGroup);
    }

    public GroupDTO getGroupById(Long id){
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Group not found"));
        return  groupMapper.toDTO(existingGroup);
    }

    public List<GroupDTO> getAllGroups(){
        List<Group> listGroups = groupRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return listGroups.stream().map(groupMapper::toDTO).toList();
    }

    public GroupDTO updateGroup(GroupDTO groupDTO, Long id){
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Group not found"));
        existingGroup.setName(groupDTO.getName());
        Group saveGroup = groupRepository.save(existingGroup);
        return groupMapper.toDTO(saveGroup);
    }

    public void deleteGroup(Long id){
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Group not found"));
        groupRepository.delete(existingGroup);
    }

    public String saveFileData(MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("Uploaded file is empty");

        }

        // Log file details
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        long fileSize = file.getSize();

        logger.info("Received file: Name={}, Type={}, Size={}", fileName, fileType, fileSize);

        // Validate file type by extension
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            logger.error("Unsupported file type: {}", fileType);
        }

        try (InputStream is = file.getInputStream()) {
            // get the list of all the object or check if the object is present in database if not present then add
            // else tell that this entry is already present
            String msg = "OKKKKK";
            return  msg;


        } catch (IOException e) {
            logger.error("IOException occurred while processing file", e);
            return "IOException occurred while processing file";

        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            return "Unexpected error occurred";

        }

    }

    public void savedFileData(InputStream file) throws IOException {
        Set<Insured> insureds = new HashSet<>();
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);

        sheet.forEach(row ->{
            if (row.getRowNum() == 0) {
                return; // Skip header row
            }
            Insured insured = Insured.builder()
                    .firstName(row.getCell(0).getStringCellValue())
                    .lastName(row.getCell(1).getStringCellValue())
                    .phoneNumber(row.getCell(2).getStringCellValue())
                    .dateOfBirth(row.getCell(3).getLocalDateTimeCellValue().toLocalDate())
                    .address(row.getCell(3).getStringCellValue())
                    .build();

            Beneficiary ben = new Beneficiary();
            Insured savedInsured = insuredRepository.save(insured);



        });
    }

    private Set<Insured> parseExcel(MultipartFile file, Group group) {



        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Set<Insured> insureds = new HashSet<>();
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Beneficiary ben = new Beneficiary();
                ben.setFirstName(group.getFirstName());
                ben.setLastName(group.getLastName());
                ben.setPhoneNumber(group.getPhoneNumber());
                ben.setDateOfBirth(group.getDateOfBirth());

                Insured insured = new Insured();
                Row row = sheet.getRow(i);
                if (row == null) continue;
                insured.setFirstName(getCellValueAsString(row.getCell(0)));
                insured.setLastName(getCellValueAsString(row.getCell(1)));
                insured.setDateOfBirth(getCellValueAsLocalDate(row.getCell(2)));
                insured.setPhoneNumber(getCellValueAsString(row.getCell(3)));
                insured.setCreatedAt(LocalDateTime.now());
                insured.setBeneficiary(ben);
                insureds.add(insured);
            }

            return insureds;
        } catch (Exception e) {
            throw new RuntimeException("Erreur de lecture du fichier Excel"+e.getMessage());
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getRichStringCellValue().getString().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // Adjust date format if needed
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private LocalDate getCellValueAsLocalDate(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC &&
                    DateUtil.isCellDateFormatted(cell)) {

                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                if (value.isEmpty()) return null;
                return LocalDate.parse(value); // yyyy-MM-dd
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public byte[] generateContractByGroup(Long groupId) throws IOException {
        Context context = new Context();
        Group group = groupRepository.findById(groupId)
                .orElseThrow(()->new ResourceNotFoundException("Group not found"));
         Contract contract =group.getInsureds().stream()
                 .map(Insured::getContract)
                 .filter(Objects::nonNull)
                 .findFirst()
                 .orElseThrow(()->new ResourceNotFoundException("Aucun contrat trouvé pour ce group"));

        byte[] logoBytesAxa = new ClassPathResource("static/logo-axa2.png")
                .getInputStream()
                .readAllBytes();

        byte[] logoBytesCnaas = new ClassPathResource("static/logo-cnaas.png")
                .getInputStream()
                .readAllBytes();

        String logoCnaasBase64 = Base64.getEncoder().encodeToString(logoBytesCnaas);

        String logoAxaBase64 = Base64.getEncoder().encodeToString(logoBytesAxa);

        context.setVariable("logoAxa", "data:image/png;base64," + logoAxaBase64);

        context.setVariable("logoCnaas", "data:image/png;base64," + logoCnaasBase64);

        context.setVariable("group", group);
        context.setVariable("contract", contract);
        String fileName = "contract_group" + contract.getId() + ".pdf";
        Path pdfPath = Paths.get(storagePath, fileName);
        String htmlContent = templateEngine.process("contract-group", context);

        try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent,null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();

        } catch (IOException e) {
            throw new IOException(e);
        }

    }
    public List<GroupDTO> getGroups(){
        User currentUser = userService.getCurrentUser();
       List<Group> groupList = new ArrayList<>();
        if(currentUser.getRole().getName().equals(RoleEnum.USER)){
            groupList = groupRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId());
        }
        else{
            groupList = groupRepository.findAll();
        }

        return groupList.stream().map(groupMapper::toDTO).toList();

    }

    public Long getNbGroups(){
        User currentUser = userService.getCurrentUser();
        RoleEnum roleAdmin = currentUser.getRole().getName();
        Long nbGroups;
        if(roleAdmin.equals(RoleEnum.ADMIN)){
            nbGroups = groupRepository.countAllGroups();
        }
        else {
            nbGroups = groupRepository.countGroupsForCurrentUser(currentUser.getId());
        }
        return nbGroups;

    }

    public GroupDTO activeGroup(Long groupId){
        User currentUser = userService.getCurrentUser();
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(()->new ResourceNotFoundException("Insured not found"));
        existingGroup.setValidatedBy(currentUser);
        existingGroup.setValidatedAt(LocalDateTime.now());
        existingGroup.setStatus(GroupStatus.ACTIF);

        existingGroup.getInsureds().forEach(insured -> {
            if(insured.getStatus()==InsuredStatus.EN_ATTENTE){
                insured.setValidatedBy(currentUser);
                insured.setValidatedAt(LocalDateTime.now());
                insured.setStatus(InsuredStatus.ACTIF);
            }
        });
        groupRepository.save(existingGroup);
        return groupMapper.toDTO(existingGroup);



    }


}
