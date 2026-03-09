package sn.axa.apiaxacnaas.services;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.Insured;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {
    public ByteArrayInputStream export(List<InsuredDTO> insureds) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    try (Workbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream out = new ByteArrayOutputStream()) {

        Sheet sheet = workbook.createSheet("Assures");

        Row headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("Prénom");
        headerRow.createCell(1).setCellValue("Nom");
        headerRow.createCell(2).setCellValue("dateOfBirth");
        headerRow.createCell(3).setCellValue("phoneNumber");
        headerRow.createCell(4).setCellValue("Status");
        headerRow.createCell(5).setCellValue("Date Souscription");

        int rowIdx = 1;

        for (InsuredDTO insured : insureds) {

            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(insured.getFirstName());
            row.createCell(1).setCellValue(insured.getLastName());
            row.createCell(2).setCellValue(insured.getDateOfBirth().format(formatter));
            row.createCell(3).setCellValue(insured.getPhoneNumber());
            row.createCell(4).setCellValue(insured.getStatus().toString());
            row.createCell(5).setCellValue(insured.getSubscriptionDate().format(formatter));

        }

        workbook.write(out);

        return new ByteArrayInputStream(out.toByteArray());

    } catch (Exception e) {
        throw new RuntimeException("Erreur export Excel");
    }
}
}