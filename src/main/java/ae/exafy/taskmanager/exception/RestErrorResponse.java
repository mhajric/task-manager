package ae.exafy.taskmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class RestErrorResponse {

    private String message;

    private LocalDateTime timestamp;
}
