package int221.kw4.clinics.advices;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler extends Exception {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HandleValidationError handleInvalidArgument(MethodArgumentNotValidException ex) {
        HandleValidationError errors = new HandleValidationError();
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error ->{
                    errorMap.put(error.getField(),error.getDefaultMessage());
                }
        );
        errors.setStatus(400);
        errors.setMessage("Bad Request");
        errors.setError("Validation Error");
        errors.setPath("/api/events");
        errors.setFiledErrors(errorMap);
        return  errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandleExceptionOverlap.class)
    public HandleException HandleOverlapError(HandleExceptionOverlap ol){
        HandleException errors = new HandleException();
        errors.setTimestamp(new Date());
        errors.setStatus(400);
        errors.setMessage("Bad Request");
        errors.setError("StartTime is Overlap");
        errors.setPath("/api/events");
        return  errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public HandleException handleUniqueCategoryName(DataIntegrityViolationException ue){
        HandleException errors = new HandleException();
        errors.setTimestamp(new Date());
        errors.setStatus(500);
        errors.setMessage("Bad Request");
        errors.setError("CategoryName should be Unique");
        errors.setPath("/api/eventCategories");
        return  errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HandleExceptionNotFound.class)
    public HandleException handleExceptionNotFound(HandleExceptionNotFound nf){
        HandleException errors = new HandleException();
        errors.setTimestamp(new Date());
        errors.setStatus(404);
        errors.setMessage("NOT FOUND");
        errors.setError(nf.getMessage());
        errors.setPath("/api/events");
        return  errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HandleExceptionNotFoundCategory.class)
    public HandleException handleExceptionNotFound(HandleExceptionNotFoundCategory nfc){
        HandleException errors = new HandleException();
        errors.setTimestamp(new Date());
        errors.setStatus(404);
        errors.setMessage("NOT FOUND");
        errors.setError(nfc.getMessage());
        errors.setPath("/api/eventCategories");
        return  errors;
    }

}
