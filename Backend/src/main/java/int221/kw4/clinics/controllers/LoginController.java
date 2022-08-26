package int221.kw4.clinics.controllers;

import int221.kw4.clinics.dtos.LoginDTO;
import int221.kw4.clinics.services.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/match")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping ("")
    public ResponseEntity login(@RequestBody LoginDTO login){
        return service.Login(login);
    }
}
