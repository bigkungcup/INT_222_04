package int221.kw4.clinics.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler extends Exception {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HandleValidationError handleInvalidArgument(MethodArgumentNotValidException ex) {
        HandleValidationError validationError = new HandleValidationError();
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error ->{
                    errorMap.put(error.getField(),error.getDefaultMessage());
                }
        );
        validationError.setFiledErrors(errorMap);
        return  validationError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandleOverlapError.class)
    public OverLapError HandleOverlapError(HandleOverlapError ol){
        OverLapError errors = new OverLapError();
        errors.setTimestamp(new Date());
        errors.setStatus(400);
        errors.setMessage("Bad Request");
        errors.setError("StartTime is Overlap");
        return  errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UnexpectedTypeException.class)
    public  HandleUniqueCategoryname handleUniqueCategoryname(UnexpectedTypeException ue){
        HandleUniqueCategoryname errors = new HandleUniqueCategoryname();
        errors.setTimestamp(new Date());
        errors.setStatus(500);
        errors.setMessage("Bad Request");
        errors.setError("CategoryName should be Unique");
        errors.setPath("/api/eventCategory");
        return  errors;
    }
}
