package int221.kw4.clinics.services;

import int221.kw4.clinics.dtos.EventPageDTO;
import int221.kw4.clinics.dtos.UserDTO;
import int221.kw4.clinics.dtos.UserPageDTO;
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
}
