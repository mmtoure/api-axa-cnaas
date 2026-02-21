package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.GroupDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.services.GroupService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")

public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO){
        GroupDTO newGroup = groupService.createGroup(groupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
    }

    @GetMapping("all")
    public  ResponseEntity<List<GroupDTO>> getAllGroups(){
        List<GroupDTO> groups = groupService.getAllGroups();
        return ResponseEntity.status(HttpStatus.OK).body(groups);
    }

    @GetMapping
    public ResponseEntity<Page<GroupDTO>> getInsureds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(groupService.getGroups(page, size));
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
        byte[] pdf = groupService.generateContractByGroup(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contrat-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


}