package int221.kw4.clinics.controllers;

import int221.kw4.clinics.dtos.LoginDTO;
import int221.kw4.clinics.services.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/match")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping ("")
    public ResponseEntity login(@RequestBody LoginDTO login, ServletWebRequest request, HttpServletResponse httpStatus){
        return service.Login(login, request, httpStatus);
    }
}
