package int221.kw4.clinics.controllers;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.dtos.UserDTO;
import int221.kw4.clinics.dtos.UserPageDTO;
import int221.kw4.clinics.dtos.UserPostDTO;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("")
    public UserPageDTO getAllUser( @RequestParam(defaultValue = "name") String sortBy,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "6") int pageSize){
        return service.getAllUser(sortBy, page, pageSize);
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable Integer userId) throws HandleExceptionNotFound{
        return service.getUserById(userId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody UserPostDTO newUser){
        return service.createUser(newUser);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Integer userId) throws HandleExceptionNotFound {
        service.deleteUser(userId);
    }
}
