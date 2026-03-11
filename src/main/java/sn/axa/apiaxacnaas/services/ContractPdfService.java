package sn.axa.apiaxacnaas.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.Contract;
import sn.axa.apiaxacnaas.entities.Insured;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ContractPdfService {

    private final TemplateEngine templateEngine;
    @Value("${app.pdf.storage-path}")
    private String storagePath;

    public byte[] generateAndSavePdf(Insured insured, String template) throws IOException {
        Context context = new Context();
        Contract contract = insured.getContract();
        if (contract == null) {
            throw new ResourceNotFoundException("No contract found for insured id=" + insured.getId());
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
