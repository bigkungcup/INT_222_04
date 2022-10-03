package int221.kw4.clinics.advices;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;
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
                error -> {
                    errorMap.put(error.getField(), error.getDefaultMessage());
                }
        );

        errors = new HandleValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Bad Request", "Validation", request.getRequest().getRequestURI(), errorMap);

        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {HandleExceptionOverlap.class})
    public HandleValidationError HandleOverlapError(HandleExceptionOverlap ol, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("eventStartTime", ol.getMessage());
        errors = new HandleValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Bad Requestss", "Validation", request.getRequest().getRequestURI(), errorMap);

        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public HandleValidationError HandleRole(ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("role", "This role does not exist");
        errors = new HandleValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Bad Request", "Validation", request.getRequest().getRequestURI(), errorMap);

        return errors;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public HandleValidationError handleUniqueCategoryName(DataIntegrityViolationException ue, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error:", ue.getLocalizedMessage());
        errors = new HandleValidationError(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error", "Validation", request.getRequest().getRequestURI(), errorMap);

        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HandleExceptionUnique.class)
    public HandleValidationError handleUniqueName(HandleExceptionUnique uq, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error:", uq.getMessage());
        errors = new HandleValidationError(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error", "Validation", request.getRequest().getRequestURI(), errorMap);
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HandleExceptionNotFound.class)
    public HandleValidationError handleExceptionNotFound(HandleExceptionNotFound nf, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error:", nf.getMessage());
        errors = new HandleValidationError(Instant.now(), HttpStatus.NOT_FOUND.value(),
                "Not Found", "Validation", request.getRequest().getRequestURI(), errorMap);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandleExceptionBadRequest.class)
    public HandleValidationError handleExceptionBadRequest(HandleExceptionBadRequest br, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error:", br.getMessage());
        errors = new HandleValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Bad Request", "Validation", request.getRequest().getRequestURI(), errorMap);
        return errors;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(HandleExceptionForbidden.class)
    public HandleValidationError handleExceptionForbidden(HandleExceptionForbidden fb, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error:", fb.getMessage());
        errors = new HandleValidationError(Instant.now(), HttpStatus.FORBIDDEN.value(),
                "Forbidden", "Validation", request.getRequest().getRequestURI(), errorMap);
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public HandleValidationError handleExceptionUserNotFound(UsernameNotFoundException notFoundException, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error:", notFoundException.getMessage());
        errors = new HandleValidationError(Instant.now(), HttpStatus.NOT_FOUND.value(),
                "Not Found", "Validation", request.getRequest().getRequestURI(), errorMap);
        return errors;
    }

}
