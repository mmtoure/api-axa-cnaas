package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.controllers.TestController;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.dto.GroupDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.Beneficiary;
import sn.axa.apiaxacnaas.entities.Group;
import sn.axa.apiaxacnaas.entities.Insured;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.mappers.ContractMapper;
import sn.axa.apiaxacnaas.mappers.GroupMapper;
import sn.axa.apiaxacnaas.mappers.InsuredMapper;
import sn.axa.apiaxacnaas.repositories.GroupRepository;
import sn.axa.apiaxacnaas.repositories.InsuredRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.log;

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

    public GroupDTO subscribeGroup(GroupDTO groupDTO, MultipartFile file ){
        Group group = groupMapper.toEntity(groupDTO);
        Group savedGroup = groupRepository.save(group);
        Set<Insured> insureds = parseExcel(file);
        if(!insureds.isEmpty()){
            insureds.forEach(insured -> {
                insured.setGroup(savedGroup);
                insuredRepository.save(insured);
                contractService.createContract(insured);
            });

        }

        return groupMapper.toDTO(savedGroup);


    }
    public GroupDTO createGroup(GroupDTO groupDTO){
        Group group = groupMapper.toEntity(groupDTO);
        Group savedGroup = groupRepository.save(group);
        if(group.getInsureds()!=null){
            group.getInsureds().forEach(insured -> {
                insured.setGroup(group);
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

            Insured savedInsured = insuredRepository.save(insured);



        });
    }

    private Set<Insured> parseExcel(MultipartFile file) {

        Set<Insured> insureds = new HashSet<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Insured insured = new Insured();
                Beneficiary ben = new Beneficiary();
                Row row = sheet.getRow(i);
                if (row == null) continue;
                insured.setFirstName(getCellValueAsString(row.getCell(0)));
                insured.setLastName(getCellValueAsString(row.getCell(1)));
                insured.setPhoneNumber(getCellValueAsString(row.getCell(3)));
                insured.setDateOfBirth(row.getCell(6).getLocalDateTimeCellValue().toLocalDate());
                insured.setCreatedAt(LocalDateTime.now());

                ben.setFirstName(getCellValueAsString(row.getCell(4)));
                ben.setLastName(getCellValueAsString(row.getCell(5)));
                ben.setDateOfBirth(row.getCell(6).getLocalDateTimeCellValue().toLocalDate());
                ben.setPhoneNumber(getCellValueAsString(row.getCell(7)));
                ben.setLienParente(getCellValueAsString(row.getCell(8)));


                insured.setBeneficiary(ben);

                ben.setInsured(insured);
                insureds.add(insured);

            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur de lecture du fichier Excel");
        }

        return insureds;
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

}
