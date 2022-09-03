package int221.kw4.clinics.controllers;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionUnique;
import int221.kw4.clinics.dtos.*;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public UserPageDTO getAllUser( @RequestParam(defaultValue = "name") String sortBy,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "6") int pageSize){
        return service.getAllUser(sortBy, page, pageSize);
    }

    @GetMapping("/userAll")
    public List<UserDTO> getAllUsers(){
        return service.getAll();
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable Integer userId) throws HandleExceptionNotFound{
        return service.getUserById(userId);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody UserPostDTO newUser) throws HandleExceptionUnique{
        return service.createUser(newUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Integer userId) throws HandleExceptionNotFound {
        return service.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@Valid @RequestBody UserEditDTO updateEvent, @PathVariable Integer userId) throws HandleExceptionUnique {
        return service.updateUser(updateEvent, userId);
    }
}
