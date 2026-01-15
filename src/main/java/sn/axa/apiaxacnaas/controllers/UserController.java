package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.UserCreateDTO;
import sn.axa.apiaxacnaas.dto.UserDTO;
import sn.axa.apiaxacnaas.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        UserDTO  userDTO = userService.getUserById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@RequestBody UserCreateDTO request, @PathVariable Long id){
        UserDTO  userDTO = userService.updateUserById(request,id);
        return  ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return  ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id){
      userService.deactivateUser(id);
        return  ResponseEntity.status(HttpStatus.OK).body("Utilisateur désactivé avec succès");
    }

    @GetMapping("/{id}/activate")
    public ResponseEntity<String> activateUser(@PathVariable Long id){
        userService.activateUser(id);
        return  ResponseEntity.status(HttpStatus.OK).body("Utilisateur activé avec succès");
    }
}
