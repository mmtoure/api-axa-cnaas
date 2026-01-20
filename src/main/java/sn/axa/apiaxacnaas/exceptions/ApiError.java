package sn.axa.apiaxacnaas.exceptions;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    private int status;
    private String message;
    private String code;
    private LocalDateTime timestamp;
    private String path;
}
