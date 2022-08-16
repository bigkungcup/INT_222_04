package int221.kw4.clinics.controllers;

import int221.kw4.clinics.dtos.UserPageDTO;
import int221.kw4.clinics.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
}
