package sn.axa.apiaxacnaas.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.Contract;
import sn.axa.apiaxacnaas.entities.Insured;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ContractPdfService {

    private final TemplateEngine templateEngine;
    @Value("${app.pdf.storage-path}")
    private String storagePath;

    public byte[] generateAndSavePdf(ContractDTO contract, InsuredDTO insured, String template) throws IOException {
        Context context = new Context();
        context.setVariable("insured", insured);
        context.setVariable("contract", contract);
        context.setVariable("garanties",contract.getGaranties() );
        String htmlContent = templateEngine.process(template, context);
        String fileName = "contract_" + contract.getId() + ".pdf";
        Path pdfPath = Paths.get(storagePath, fileName);

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
