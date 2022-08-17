package int221.kw4.clinics.services;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionUnique;
import int221.kw4.clinics.dtos.*;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public UserDTO getUserById(Integer userId) throws HandleExceptionNotFound{
        User userById = repository.findById(userId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "User ID: " + userId + " does not exist !!!"));
        return modelMapper.map(userById, UserDTO.class);
    }

    public User createUser(UserPostDTO newUser){
        List<User> userList = repository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if(newUser.getName().equals(userList.get(i).getName())){
                throw new RuntimeException("Name should be Unique");
            } else if (newUser.getEmail().equals(userList.get(i).getEmail())) {
                throw new RuntimeException("Email should be Unique");
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
}
