package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.GroupDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.services.ContractPdfService;
import sn.axa.apiaxacnaas.services.GroupService;

import java.io.IOException;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")

public class GroupController {
    private final GroupService groupService;
    private final ContractPdfService contractPdfService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GroupDTO> createGroup(
            @RequestPart("group") GroupDTO groupDTO,
            @RequestPart("file") MultipartFile file,
            @RequestPart("proofPayment")  MultipartFile proofPayment

    ) throws IOException {
        GroupDTO newGroup = groupService.subscribeGroup(groupDTO,file,proofPayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
    }


    @GetMapping
    public  ResponseEntity<List<GroupDTO>> getAllGroups(){
        List<GroupDTO> groups = groupService.getAllGroups();
        return ResponseEntity.status(HttpStatus.OK).body(groups);
    }

    @GetMapping("all")
    public ResponseEntity<List<GroupDTO>> getGroups(

    ) {
        return ResponseEntity.ok(groupService.getGroups());
    }

    @GetMapping("/{id}")
    public  ResponseEntity<GroupDTO> getGroupById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupById(id));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<GroupDTO> updateGroup(@RequestBody GroupDTO groupDTO, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(groupService.updateGroup(groupDTO,id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteGroup(@PathVariable Long id){
        groupService.deleteGroup(id);
        return ResponseEntity.status(HttpStatus.OK).body("Group supprimée avec succés");
    }
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateContractPdf(@PathVariable Long id) throws IOException {
        byte[] pdf = contractPdfService.generatePdfForGroup(id);
        String fileName = "fiche_adhesion_group" + id + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/{id}/valider")
    public ResponseEntity<GroupDTO> validateInsured(@PathVariable Long id){
        GroupDTO groupDTO = groupService.activeGroup(id);
        return ResponseEntity.status(HttpStatus.OK).body(groupDTO);
    }



}