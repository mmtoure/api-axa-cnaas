package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.GroupDTO;
import sn.axa.apiaxacnaas.services.GroupService;

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

    @GetMapping
    public  ResponseEntity<List<GroupDTO>> getAllGroups(){
        List<GroupDTO> groups = groupService.getAllGroups();
        return ResponseEntity.status(HttpStatus.OK).body(groups);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<GroupDTO> getGroupById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getFroupById(id));
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


}