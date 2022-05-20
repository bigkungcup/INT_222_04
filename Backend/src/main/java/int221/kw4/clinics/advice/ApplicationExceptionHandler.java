package int221.kw4.clinics.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler extends Exception {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationError handleInvalidArgument(MethodArgumentNotValidException ex) {
        ValidationError validationError = new ValidationError();
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error ->{
//                    String fieldName = error.getField();
//                    String errorMessage = error.getDefaultMessage();
                    errorMap.put(error.getField(),error.getDefaultMessage());
                }
        );
        validationError.setFiledErrors(errorMap);
        return  validationError;
    }
}
