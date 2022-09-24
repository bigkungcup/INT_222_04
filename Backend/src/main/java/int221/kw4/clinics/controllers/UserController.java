package int221.kw4.clinics.controllers;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionUnique;
import int221.kw4.clinics.dtos.*;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.services.LoginService;
import int221.kw4.clinics.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;



@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    private final LoginService loginService;

    public UserController(UserService service,  LoginService loginService) {
        this.service = service;
        this.loginService = loginService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('admin')")
    public UserPageDTO getAllUser( @RequestParam(defaultValue = "name") String sortBy,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "6") int pageSize){
        return service.getAllUser(sortBy, page, pageSize);
    }

    @GetMapping("/userAll")
    @PreAuthorize("hasRole('admin')")
    public List<UserDTO> getAllUsers(){
        return service.getAll();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    public UserDTO getUserById(@PathVariable Integer userId) throws HandleExceptionNotFound{
        return service.getUserById(userId);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody UserPostDTO newUser) throws HandleExceptionUnique{
        return service.createUser(newUser);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Integer userId) throws HandleExceptionNotFound {
        return service.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@Valid @RequestBody UserEditDTO updateEvent, @PathVariable Integer userId) throws HandleExceptionUnique {
        return service.updateUser(updateEvent, userId);
    }

    @PostMapping("/match")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity login(@RequestBody LoginDTO login, ServletWebRequest request, HttpServletResponse httpStatus) {
        return loginService.MatchPassword(login, request, httpStatus);
    }
}
