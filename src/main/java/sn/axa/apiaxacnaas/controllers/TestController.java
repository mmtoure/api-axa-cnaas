package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.dto.GroupDTO;
import sn.axa.apiaxacnaas.services.GroupService;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final GroupService groupService;
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @PostMapping(
            value = "/uploadExcel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadFileExcel(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.subscribeGroup(file));

    }
}
