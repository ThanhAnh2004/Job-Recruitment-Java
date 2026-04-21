package thanhanh.job_recruitment.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import thanhanh.job_recruitment.dto.response.ApiResponse.RestResponse;

import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdException (Exception exception) {
        RestResponse<Object> res = new RestResponse<>();
        res.setError(exception.getMessage());
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("Exception occurs...");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationException (MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(exception.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).toList();
        res.setMessage(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFoundException (NoResourceFoundException exception) {
        RestResponse<Object> res = new RestResponse<>();
        res.setError(exception.getMessage());
        res.setMessage("Not found resource. UML not exist");
        res.setStatusCode(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = StorageException.class)
    public ResponseEntity<RestResponse<Object>> handleUploadFileException (StorageException exception) {
        RestResponse<Object> response = new RestResponse<>();

        response.setError(exception.getMessage());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Exception upload file...");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = PermissionException.class)
    public ResponseEntity<RestResponse<Object>> handlePermissionException (PermissionException exception) {
        RestResponse<Object> response = new RestResponse<>();

        response.setError(exception.getMessage());
        response.setStatusCode(HttpStatus.FORBIDDEN.value());
        response.setMessage("Forbidden...");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}
