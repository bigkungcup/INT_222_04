package int221.kw4.clinics.controllers;

import int221.kw4.clinics.advices.HandleExceptionForbidden;
import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionUnique;
import int221.kw4.clinics.dtos.securities.LoginDTO;
import int221.kw4.clinics.dtos.users.UserDTO;
import int221.kw4.clinics.dtos.users.UserEditDTO;
import int221.kw4.clinics.dtos.users.UserPageDTO;
import int221.kw4.clinics.dtos.users.UserPostDTO;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.services.LoginService;
import int221.kw4.clinics.services.UserService;
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

    public UserController(UserService service, LoginService loginService) {
        this.service = service;
        this.loginService = loginService;
    }

    @GetMapping("")
    public UserPageDTO getAllUser(@RequestParam(defaultValue = "name") String sortBy,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "6") int pageSize) {
        return service.getAllUser(sortBy, page, pageSize);
    }

    @GetMapping("/userAll")
    public List<UserDTO> getAllUsers() {
        return service.getAll();
    }

    @GetMapping("/{userId}")
//    @PreAuthorize("hasRole('admin') or hasRole('lecturer') or hasRole('student')")
    public UserDTO getUserById(@PathVariable(required = false) Integer userId) throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.getUserById(userId);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody UserPostDTO newUser) throws HandleExceptionUnique {
        return service.createUser(newUser);
    }

    @PostMapping("/register/{userId}/eventCategory")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createCategory(@PathVariable Integer userId, @RequestBody EventCategory eventCategory)
            throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.addEventCategoty(userId, eventCategory);
    }

    @PostMapping("/match")
    public ResponseEntity login(@RequestBody LoginDTO login, ServletWebRequest request, HttpServletResponse httpStatus) {
        return loginService.MatchPassword(login, request, httpStatus);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Integer userId) throws HandleExceptionNotFound {
        return service.deleteUser(userId);
    }

    @DeleteMapping("/{userId}/eventCategory/{eventCategoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteCategory(@PathVariable Integer userId, @PathVariable Integer eventCategoryId)
            throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.deleteEventCategoryUser(userId, eventCategoryId);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@Valid @RequestBody UserEditDTO updateEvent, @PathVariable Integer userId) throws HandleExceptionUnique {
        return service.updateUser(updateEvent, userId);
    }


}
