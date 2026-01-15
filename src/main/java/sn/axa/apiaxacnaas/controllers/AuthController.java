package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sn.axa.apiaxacnaas.dto.LoginDTO;
import sn.axa.apiaxacnaas.dto.UserCreateDTO;
import sn.axa.apiaxacnaas.dto.UserDTO;
import sn.axa.apiaxacnaas.services.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<UserDTO> createChefAgence(@RequestBody UserCreateDTO request) {
        UserDTO newUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO request) {
        Map<String, Object> response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(){
        UserDTO currentUser = userService.getPublicUser(null);
        return ResponseEntity.ok(currentUser);
    }


}
