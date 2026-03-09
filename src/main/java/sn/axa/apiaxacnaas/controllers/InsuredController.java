package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.FilterDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.services.ExcelExportService;
import sn.axa.apiaxacnaas.services.InsuredService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/insureds")

public class InsuredController {
    private final InsuredService insuredService;
    private final ExcelExportService excelExportService;

    @PostMapping
    public ResponseEntity<InsuredDTO> createInsured(@RequestBody InsuredDTO insuredDTODTO){
        InsuredDTO newInsured = insuredService.createInsured(insuredDTODTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newInsured);
    }

    @GetMapping("all")
    public  ResponseEntity<List<InsuredDTO>> getAllInsureds(){
        List<InsuredDTO> allInsureds = insuredService.getAllInsureds();
        return ResponseEntity.status(HttpStatus.OK).body(allInsureds);
    }

    @GetMapping
    public ResponseEntity<Page<InsuredDTO>> getInsureds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(insuredService.getInsureds(page, size));
    }


    @GetMapping("/{id}")
    public  ResponseEntity<InsuredDTO> getInsuredById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(insuredService.getInsuredById(id));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<InsuredDTO> updateInsured(@RequestBody InsuredDTO insuredDTODTO, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(insuredService.updateInsured(insuredDTODTO,id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteInsured(@PathVariable Long id){
        insuredService.deleteInsured(id);
        return ResponseEntity.status(HttpStatus.OK).body("Insured supprimée avec succés");
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateContractPdf(@PathVariable Long id) throws IOException {
        byte[] pdf = insuredService.generateContractByInsured(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contrat-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<InsuredDTO>> filterInsureds(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate)
    {
        List<InsuredDTO> listInsureds = insuredService.filter(startDate, endDate);
        return new ResponseEntity<>(listInsureds, HttpStatus.OK);

    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportInsureds(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate)
    {
        List<InsuredDTO> listInsureds = insuredService.filter(startDate, endDate);
        ByteArrayInputStream excel = excelExportService.export(listInsureds);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", "attachment; filename=assures.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(excel));
    }



}