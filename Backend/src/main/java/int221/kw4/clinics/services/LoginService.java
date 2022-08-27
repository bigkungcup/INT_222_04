package int221.kw4.clinics.services;

import int221.kw4.clinics.advices.HandleValidationError;
import int221.kw4.clinics.dtos.LoginDTO;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.repositories.UserRepository;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    HandleValidationError errors = new HandleValidationError();

    public LoginService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity Login(LoginDTO login, ServletWebRequest request, HttpServletResponse httpStatus) {
        Map<String, String> errorMap = new HashMap<>();
        String status = "";
        if (repository.findUserByEmail(login.getEmail()) != null) {
            User user = repository.findUserByEmail(login.getEmail());
            if (passwordEncoder.matches(login.getPassword(), user.getPassword())) {
                errorMap.put("Login: ", "Successes");
                httpStatus.setStatus(HttpStatus.OK.value());
                status = "Ok";
            } else {
                errorMap.put("Login: ", "Failed");
                httpStatus.setStatus(HttpStatus.UNAUTHORIZED.value());
                status = "Unauthorized";
            }
        } else {
            errorMap.put("Error: ", "User dose not exist");
            httpStatus.setStatus(HttpStatus.NOT_FOUND.value());
            status = "Not Found";
        }

        errors = new HandleValidationError(Instant.now(), httpStatus.getStatus(),
                status, "Validation", request.getRequest().getRequestURI(), errorMap);

        return ResponseEntity.status(httpStatus.getStatus()).body(errors);
    }
}
