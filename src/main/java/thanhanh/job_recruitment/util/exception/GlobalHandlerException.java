package thanhanh.job_recruitment.util.exception;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import thanhanh.job_recruitment.dto.response.RestResponse;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdException (IdInvalidException invalidException) {
        RestResponse<Object> res = new RestResponse<>();
        res.setError(invalidException.getMessage());
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("IdInvalidException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
