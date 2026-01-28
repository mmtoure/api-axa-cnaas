package sn.axa.apiaxacnaas.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.*;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.InsuredMapper;
import sn.axa.apiaxacnaas.repositories.InsuredRepository;
import sn.axa.apiaxacnaas.repositories.UserRepository;
import sn.axa.apiaxacnaas.util.InsuredStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuredService {
    private static final Logger log = LoggerFactory.getLogger(InsuredService.class);
    private final InsuredMapper insuredMapper;
    private final InsuredRepository insuredRepository;
    private final UserService userService;
    private final ContractService contractService;
    private final TemplateEngine templateEngine;
    @Value("${app.pdf.storage-path}")
    private String storagePath;


    public InsuredDTO createInsured(InsuredDTO insuredDTO){
        User currentUser = userService.getCurrentUser();
        Insured insured = insuredMapper.toEntity(insuredDTO);
        insured.setUser(currentUser);
        insured.setStatus(InsuredStatus.ACTIF);
        if(insured.getBeneficiary()!=null){
            insured.getBeneficiary().setInsured(insured);
        }
        Insured savedInsured = insuredRepository.save(insured);
        contractService.createContract(savedInsured);
        return insuredMapper.toDTO(savedInsured);
    }

    public InsuredDTO getInsuredById(Long id){
        Insured existingInsured = insuredRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Insured not found"));
        return  insuredMapper.toDTO(existingInsured);
    }

    public List<InsuredDTO> getAllInsureds(){
        List<Insured> listInsureds = insuredRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return listInsureds.stream().map(insuredMapper::toDTO).toList();
    }

    public InsuredDTO updateInsured(InsuredDTO insuredDTO, Long id){
        Insured existingInsured = insuredRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Insured not found"));
        Insured saveInsured = insuredRepository.save(insuredMapper.toEntity(insuredDTO));
        return insuredMapper.toDTO(saveInsured);

    }

    public void deleteInsured(Long id){
        Insured existingInsured = insuredRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Insured not found"));
        insuredRepository.delete(existingInsured);
    }

    public byte[] generateContractByInsured(Long insuredId) throws IOException {
        Context context = new Context();
        Insured insured = insuredRepository.findById(insuredId)
                .orElseThrow(()->new ResourceNotFoundException("Insured not found"));
        Contract contract = insured.getContract();
        if (contract == null) {
            throw new ResourceNotFoundException("No contract found for insured id=" + insuredId);
        }
        context.setVariable("insured", insured);
        context.setVariable("contract", contract);

        byte[] logoBytesAxa = new ClassPathResource("static/logo-axa.png")
                .getInputStream()
                .readAllBytes();

        byte[] logoBytesCnaas = new ClassPathResource("static/logo-cnaas.png")
                .getInputStream()
                .readAllBytes();

        String logoCnaasBase64 = Base64.getEncoder().encodeToString(logoBytesCnaas);
        String logoAxaBase64 = Base64.getEncoder().encodeToString(logoBytesAxa);
        context.setVariable("logoAxa", "data:image/png;base64," + logoAxaBase64);
        context.setVariable("logoCnaas", "data:image/png;base64," + logoCnaasBase64);
        String fileName = "contract_" + contract.getId() + ".pdf";
        Path pdfPath = Paths.get(storagePath, fileName);
        String htmlContent = templateEngine.process("contract", context);

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


}
