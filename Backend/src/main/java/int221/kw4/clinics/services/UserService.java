package int221.kw4.clinics.services;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionUnique;
import int221.kw4.clinics.dtos.*;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, ModelMapper modelMapper, ListMapper listMapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserPageDTO getAllUser(String sortBy, Integer page, Integer pageSize) {
        return modelMapper.map(repository.findAll(
                        PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy))),
                UserPageDTO.class);
    }

    public List<UserDTO> getAll() {
        List<User> userList = repository.findAll();
        return listMapper.mapList(userList, UserDTO.class, modelMapper);
    }

    public UserDTO getUserById(Integer userId) throws HandleExceptionNotFound {
        User userById = repository.findById(userId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "User ID: " + userId + " does not exist !!!"));
        return modelMapper.map(userById, UserDTO.class);
    }

    public ResponseEntity createUser(UserPostDTO newUser) throws HandleExceptionUnique {
        List<User> userList = repository.findAll();

        newUser.setName(newUser.getName().trim());
        newUser.setEmail(newUser.getEmail().trim());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        for (int i = 0; i < userList.size(); i++) {
            if (newUser.getName().equals(userList.get(i).getName()) && newUser.getEmail().equals(userList.get(i).getEmail())) {
                throw new HandleExceptionUnique("Name should be unique and email should be unique");
            } else if (newUser.getName().equals(userList.get(i).getName())) {
                throw new HandleExceptionUnique("Name should be Unique");
            } else if (newUser.getEmail().equals(userList.get(i).getEmail())) {
                throw new HandleExceptionUnique("Email should be Unique");
            }

        }
        User user = modelMapper.map(newUser, User.class);
        repository.saveAndFlush(user);
        user.setPassword("**********");
        return ResponseEntity.status(201).body(user);
    }

    public ResponseEntity deleteUser(Integer userId) throws HandleExceptionNotFound {
        repository.findById(userId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "User ID: " + userId + " does not exist !!!")
        );
        repository.deleteById(userId);
        return ResponseEntity.status(200).body("Delete UserID:" + userId);
    }

    public ResponseEntity updateUser(UserEditDTO updateUser, Integer userId) throws HandleExceptionUnique {
        List<UserDTO> userList = getAll();

        updateUser.setName(updateUser.getName().trim());
        updateUser.setEmail(updateUser.getEmail().trim());

        User user = repository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId() != userId) {
                if (updateUser.getName().equals(userList.get(i).getName()) && updateUser.getEmail().equals(userList.get(i).getEmail())) {
                    throw new HandleExceptionUnique("Name should be unique and email should be unique");
                } else if (updateUser.getName().equals(userList.get(i).getName())) {
                    throw new HandleExceptionUnique("Name should be Unique");
                } else if (updateUser.getEmail().equals(userList.get(i).getEmail())) {
                    throw new HandleExceptionUnique("Email should be Unique");
                }
            } else if (updateUser.getName().equals(userList.get(i).getName()) &&
                    updateUser.getEmail().equals(userList.get(i).getEmail()) &&
                    updateUser.getRole().equals(userList.get(i).getRole())
            ) {
                return ResponseEntity.status(200).body("The entered data has the same value.");
            }
        }

        modelMapper.map(updateUser, user);
        repository.saveAndFlush(user);
        return ResponseEntity.status(200).body(user);
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
