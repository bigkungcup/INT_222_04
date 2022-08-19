package int221.kw4.clinics.services;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionUnique;
import int221.kw4.clinics.dtos.*;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

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

    public User createUser(UserPostDTO newUser) throws HandleExceptionUnique {
        List<User> userList = repository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (newUser.getName().equals(userList.get(i).getName())) {
                throw new HandleExceptionUnique("Name should be Unique");
            } else if (newUser.getEmail().equals(userList.get(i).getEmail())) {
                throw new HandleExceptionUnique("Email should be Unique");
            }
        }
        User user = modelMapper.map(newUser, User.class);
        return repository.saveAndFlush(user);
    }

    public void deleteUser(Integer userId) throws HandleExceptionNotFound {
        repository.findById(userId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "User ID: " + userId + " does not exist !!!")
        );
        repository.deleteById(userId);
    }

    public ResponseEntity updateUser(UserEditDTO updateUser, Integer userId) throws HandleExceptionUnique {
        List<UserDTO> userList = getAll();

        User user = repository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId() != userId) {
                if (updateUser.getName().equals(userList.get(i).getName())) {
                    throw new HandleExceptionUnique("Name should be Unique");
                } else if (updateUser.getEmail().equals(userList.get(i).getEmail())) {
                    throw new HandleExceptionUnique("Email should be Unique");
                }
            }
            else if(updateUser.getName().equals(userList.get(i).getName()) ||
                    updateUser.getEmail().equals(userList.get(i).getEmail())){
                return ResponseEntity.status(200).body("Hello");
            }
        }

        modelMapper.map(updateUser, user);
        repository.saveAndFlush(user);
        return ResponseEntity.status(200).body(user);
    }
}