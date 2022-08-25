package int221.kw4.clinics.controllers;

import int221.kw4.clinics.dtos.LoginDTO;
import int221.kw4.clinics.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserService service;

    public LoginController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity login(@RequestBody LoginDTO login){
        return service.Login(login);
    }
}
