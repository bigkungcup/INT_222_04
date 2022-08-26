package int221.kw4.clinics.services;

import int221.kw4.clinics.dtos.LoginDTO;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity Login(LoginDTO login) {
        if (repository.findUserByEmail(login.getEmail()) != null) {
            User user = repository.findUserByEmail(login.getEmail());
            if (passwordEncoder.matches(login.getPassword(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.OK, "Login Successes");
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login Failed");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User dose not exist");
        }
    }
}
