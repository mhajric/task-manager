package ae.exafy.taskmanager.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(InvalidTaskException.class)
    public ResponseEntity<RestErrorResponse> handleInvalidTaskException(InvalidTaskException e) {
        return new ResponseEntity<>(new RestErrorResponse(e.getMessage(), LocalDateTime.now()), HttpStatus.OK); // we are using OK for business logic validation error
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<RestErrorResponse> handleTaskNotFoundException(TaskNotFoundException e) {
        return new ResponseEntity<>(new RestErrorResponse(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        return new ResponseEntity<>(new RestErrorResponse("Validation failed: " + errors, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    // Handle any other exception too.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorResponse> handleException(Exception ex) {
        return new ResponseEntity<>(new RestErrorResponse(
                ex.getMessage(),
                LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
