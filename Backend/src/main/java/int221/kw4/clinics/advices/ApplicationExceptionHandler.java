package int221.kw4.clinics.advices;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler extends Exception {

    HandleValidationError errors = new HandleValidationError();
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public HandleValidationError handleInvalidArgument(MethodArgumentNotValidException ex, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error ->{
                    errorMap.put(error.getField(),error.getDefaultMessage());
                }
        );

        errors = new HandleValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Bad Request","Validation Error", request.getRequest().getRequestURI(), errorMap);

        return  errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {HandleExceptionOverlap.class})
    public HandleValidationError HandleOverlapError(HandleExceptionOverlap ol, ServletWebRequest request){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("eventStartTime", ol.getMessage());

        errors = new HandleValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Bad Requestss","Validation Error", request.getRequest().getRequestURI(), errorMap);

        return  errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public HandleValidationError HandleRole(ServletWebRequest request){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("role", "This role does not exist");

        errors = new HandleValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Bad Request","Validation Error", request.getRequest().getRequestURI(), errorMap);
        return  errors;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public HandleException handleUniqueCategoryName(DataIntegrityViolationException ue){
        HandleException errors = new HandleException();
        errors.setTimestamp(new Date());
        errors.setStatus(500);
        errors.setMessage("INTERNAL SERVER ERROR");
        errors.setError("CategoryName should be Unique");
        errors.setPath("/api/eventCategories");
        return  errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HandleExceptionUnique.class)
    public HandleValidationError handleUniqueName(HandleExceptionUnique uq,ServletWebRequest request){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error:", uq.getMessage());
        errors = new HandleValidationError(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error","Validation Error", request.getRequest().getRequestURI(), errorMap);
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
