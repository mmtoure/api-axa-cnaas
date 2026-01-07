package sn.axa.apiaxacnaas;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sn.axa.apiaxacnaas.dto.RequestLogin;
import sn.axa.apiaxacnaas.dto.UserDTO;
import sn.axa.apiaxacnaas.services.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<UserDTO> createChefAgence(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody RequestLogin request) {
        Map<String, Object> response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(){
        UserDTO currentUser = userService.getPublicUser(null);
        return ResponseEntity.ok(currentUser);

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllusers(){
        List<UserDTO> users = userService.getAllusers();
        return ResponseEntity.ok(users);
    }
}
