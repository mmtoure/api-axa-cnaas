package sn.axa.apiaxacnaas.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import sn.axa.apiaxacnaas.entities.*;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.repositories.GroupRepository;
import sn.axa.apiaxacnaas.repositories.InsuredRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContractPdfService {

    private final TemplateEngine templateEngine;

    @Value("${app.pdf.storage-path}")
    private String storagePath;
    private final InsuredRepository insuredRepository;
    private final GroupRepository groupRepository;
    private String logoAxaBase64;
    private String logoCnaasBase64;
    private String logoLGBase64;
    private final UserService userService;

    @PostConstruct
    public void init() throws IOException {
        byte[] logoBytesAxa = new ClassPathResource("static/images/logo-axa.png")
                .getInputStream()
                .readAllBytes();

        byte[] logoBytesCnaas = new ClassPathResource("static/images/logo-cnaas.png")
                .getInputStream()
                .readAllBytes();

        byte[] logoBytesLG = new ClassPathResource("static/images/logo-lg.png")
                .getInputStream()
                .readAllBytes();

        logoAxaBase64 = Base64.getEncoder()
                .encodeToString(logoBytesAxa)
                .replaceAll("\\s", "");

        logoCnaasBase64 = Base64.getEncoder()
                .encodeToString(logoBytesCnaas)
                .replaceAll("\\s", "");

        logoLGBase64 = Base64.getEncoder()
                .encodeToString(logoBytesLG)
                .replaceAll("\\s", "");
    }

    public byte[] generatePdfForInsured(Long id) throws IOException {
        User currentUser = userService.getCurrentUser();
        Partner currentPartner = currentUser.getPartner();
        Context context = new Context();
        Insured insured = insuredRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insured not found"));
        Contract contract = insured.getContract();
        context.setVariable("insured", insured);
        context.setVariable("partner", insured.getPartner());
        context.setVariable("contract", contract);

        byte[] generatePdf = null;
        if(Objects.equals(currentPartner.getCode(), "CNAAS")){
            context.setVariable("logoAxa", "data:image/png;base64," + logoAxaBase64);
            context.setVariable("logoCnaas", "data:image/png;base64," + logoCnaasBase64);
            generatePdf = generatePdf("contract", context);
        }
        if(Objects.equals(currentPartner.getCode(), "LG")) {
            context.setVariable("logoAxa", "data:image/png;base64," + logoAxaBase64);
            context.setVariable("logoLg", "data:image/png;base64," + logoLGBase64);
            generatePdf= generatePdf("contract-lg", context);
        }

        return  generatePdf;





    }

    public byte[] generatePdfForGroup(Long id) throws IOException {

        Context context = new Context();
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No group found for insured id=" + id));
        context.setVariable("group", group);
        Contract contract = group.getInsureds().stream()
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("No contract"))
                .getContract();
        context.setVariable("contract", contract);
        context.setVariable("group", group);
        context.setVariable("logoAxa", "data:image/png;base64," + logoAxaBase64);
        context.setVariable("logoCnaas", "data:image/png;base64," + logoCnaasBase64);
        return generatePdf("contract-group", context);
    }

    private byte[] generatePdf(String template, Context context) throws IOException {

        String htmlContent = templateEngine.process(template, context);
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent,null);
            builder.toStream(os);
            builder.run();
            byte[] contractPdf = os.toByteArray();
            return mergeWithConditions(contractPdf);


        } catch (IOException e) {
            throw new IOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    private byte[] mergeWithConditions(byte[] contractPdf) throws Exception {
        Partner currentPartner = userService.getCurrentUser().getPartner();

        PDFMergerUtility merger = new PDFMergerUtility();
        ByteArrayOutputStream mergedOutput = new ByteArrayOutputStream();
        merger.addSource(new ByteArrayInputStream(contractPdf));
        if(Objects.equals(currentPartner.getCode(), "CNAAS")) {
            merger.addSource(
                    new ClassPathResource("/static/pdf/complement_contrat_cnaas.pdf")
                            .getInputStream()
            );
        }

        if(Objects.equals(currentPartner.getCode(), "LG")) {
            merger.addSource(
                    new ClassPathResource("/static/pdf/complement_bulletin_LG.pdf")
                            .getInputStream()
            );
        }

        merger.setDestinationStream(mergedOutput);
        merger.mergeDocuments(null);
        return mergedOutput.toByteArray();
    }

}
