package int221.kw4.clinics.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import int221.kw4.clinics.advices.HandleExceptionBadRequest;
import int221.kw4.clinics.advices.HandleExceptionForbidden;
import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionUnique;
import int221.kw4.clinics.dtos.users.*;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.entities.Role;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.repositories.EventCategoryRepository;
import int221.kw4.clinics.repositories.UserRepository;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final EventCategoryRepository eventCategoryRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, EventCategoryRepository eventCategoryRepository, EmailService emailService, ModelMapper modelMapper, ListMapper listMapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
        this.passwordEncoder = passwordEncoder;
    }

    //GET
    public UserPageDTO getAllUser(String sortBy, Integer page, Integer pageSize) {
        return modelMapper.map(repository.findAll(
                        PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy))),
                UserPageDTO.class);
    }

    public List<UserDTO> getAll() {
        List<User> userList = repository.findAll();
        return listMapper.mapList(userList, UserDTO.class, modelMapper);
    }

    public UserDTO getUserById(Integer userId) throws HandleExceptionNotFound, HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = repository.findByEmail(auth.getPrincipal().toString());

        if (user.getRole().toString().equals("admin")) {
            Integer finalUserId = userId;
            User userById = repository.findById(userId).orElseThrow(
                    () -> new HandleExceptionNotFound(
                            "User ID: " + finalUserId + " does not exist !!!"));
            return modelMapper.map(userById, UserDTO.class);
        } else {
            userId = user.getId();
            if (user.getId() == userId) {
                return modelMapper.map(repository.findById(userId).orElseThrow(() -> new HandleExceptionNotFound("User not found")), UserDTO.class);
            } else {
                throw new HandleExceptionForbidden("You don't have permission to access this resource");
            }
        }
    }

    public User getUserByEmail(String email) {
        User userByEmail = repository.findByEmail(email);
        return modelMapper.map(userByEmail, User.class);
    }

    public boolean checkEventInUser(Integer userId) throws HandleExceptionNotFound {
        UserDTO userDTO = modelMapper.map(repository.findById(userId).orElseThrow(() -> new HandleExceptionNotFound("User not found")), UserDTO.class);
        if (userDTO.getEvents().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    //POST
    public ResponseEntity createUser(UserPostDTO newUser) throws HandleExceptionUnique {
        List<User> userList = repository.findAll();

        newUser.setName(newUser.getName().trim());
        newUser.setEmail(newUser.getEmail().trim());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        for (User value : userList) {
            if (newUser.getName().equals(value.getName()) && newUser.getEmail().equals(value.getEmail())) {
                throw new HandleExceptionUnique("Name should be unique and email should be unique");
            } else if (newUser.getName().equals(value.getName())) {
                throw new HandleExceptionUnique("Name should be Unique");
            } else if (newUser.getEmail().equals(value.getEmail())) {
                throw new HandleExceptionUnique("Email should be Unique");
            }
        }

        User user = modelMapper.map(newUser, User.class);
        repository.saveAndFlush(user);
        user.setPassword("**********");

//        emailService.sendSimpleMail(user.getEmail(), "Welcome to Clinic",
//                "Welcome User: " + user.getName() + "\n" + "Your email: " + user.getEmail() + "\n" +
//                        "Your role: " + user.getRole() + "\n" + "Create at: " + user.getCreatedOn());

        return ResponseEntity.status(201).body(user);
    }

    public ResponseEntity addEventCategoryToUser(Integer userId, Integer eventCategoryId) throws HandleExceptionNotFound {
        User user = repository.findById(userId).orElseThrow(() -> new HandleExceptionNotFound("User not found"));
        EventCategory eventCategory = eventCategoryRepository.findById(eventCategoryId).orElseThrow(() -> new HandleExceptionNotFound("Event Category not found"));

        if(user.getRole().toString().equals("student") || user.getRole().toString().equals("admin")){
           return ResponseEntity.status(400).body("the owner must have lecturer role");
        }

        user.getEventCategories().stream().map(EventCategory::getId).forEach(id -> {
            if (id.equals(eventCategoryId)) {
                try {
                    throw new HandleExceptionBadRequest("Event Category ID: " + eventCategoryId + " already exists !!!");
                } catch (HandleExceptionBadRequest e) {
                    throw new RuntimeException(e);
                }
            }
        });
        user.getEventCategories().add(eventCategory);
        repository.saveAndFlush(user);
        UserLecteurDTO lecturer = modelMapper.map(user, UserLecteurDTO.class);
//        emailService.sendSimpleMail(user.getEmail(), "Add Category To User Successfully",
//                "Time at: " + new Date() + "User: " + user.getName() + "\n" +
//                        "Your email: " + user.getEmail() + "\n" + "Category: " + eventCategory.getEventCategoryName() + "\n" +
//                        "Your role: " + user.getRole(), new Date());
        return ResponseEntity.status(201).body(lecturer);
    }

    //DELETE
    public ResponseEntity deleteUser(Integer userId) throws HandleExceptionNotFound {
        User user = repository.findById(userId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "User ID: " + userId + " does not exist !!!")
        );

        List<User> allUser = repository.findAll();
        ArrayList<Integer> categories = new ArrayList<>();
        ArrayList<Integer> userCategories = new ArrayList<>();
        for (User u : allUser) {
            u.getEventCategories().stream().map(EventCategory::getId).forEach(categories::add);
        }

        user.getEventCategories().stream().map(EventCategory::getId).forEach(userCategories::add);
        System.out.println(categories);
        System.out.println(userCategories);

        for (Integer i : userCategories) {
            if (categories.stream().filter(id -> id.equals(i)).count() == 1) {
                return ResponseEntity.status(400).body("Cannot delete this category because category is not less than 1");
            }
        }

        repository.deleteById(userId);
//        emailService.sendSimpleMail(user.getEmail(), "Delete User Successfully",
//                "Time at: " + new Date() + "User: " + user.getName() + "\n" +
//                        "Your email: " + user.getEmail() + "\n" + "Your role: " + user.getRole(), new Date());

        return ResponseEntity.status(200).body("Delete UserID:" + userId);
    }

    public ResponseEntity deleteEventCategoryUser(Integer userId, Integer eventCategoryId) throws HandleExceptionNotFound, HandleExceptionForbidden, HandleExceptionBadRequest {
        User user = repository.findById(userId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "User ID: " + userId + " does not exist !!!")
        );

        List<User> allUser = repository.findAll();
        ArrayList<Integer> categories = new ArrayList<>();
        for (User u : allUser) {
            u.getEventCategories().stream().map(EventCategory::getId).forEach(categories::add);
        }

        if (categories.stream().filter(id -> id.equals(eventCategoryId)).count() == 1) {
            return ResponseEntity.status(400).body("Cannot delete this category because category is not less than 1");
        }

        if(categories.contains(eventCategoryId)){
            user.getEventCategories().removeIf(eventCategory -> eventCategory.getId().equals(eventCategoryId));
            repository.saveAndFlush(user);
        }else{
            throw new HandleExceptionBadRequest("Event Category ID: " + eventCategoryId + " does not exist !!!");
        }

        UserLecteurDTO lecturer = modelMapper.map(user, UserLecteurDTO.class);
//        emailService.sendSimpleMail(user.getEmail(), "Delete Category in User Successfully",
//                "Time at: " + new Date() + "User: " + user.getName() + "\n" +
//                        "Your email: " + user.getEmail() + "\n" + "Your role: " + user.getRole(), new Date());
        return ResponseEntity.status(200).body(lecturer);
    }

    //UPDATE
    @SneakyThrows
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
//        emailService.sendSimpleMail(user.getEmail(), "Update User Successfully",
//                "Time at: " + new Date() + "User: " + user.getName() + "\n" +
//                        "Your email: " + user.getEmail() + "\n" + "Your role: " + user.getRole(), new Date());
        return ResponseEntity.status(200).body(user);
    }

    public ResponseEntity loginWithMicrosoft(HttpServletRequest request, HttpServletResponse response) {
//        List<User> userList = repository.findAll();
//        User userDetail = repository.findByEmail(user.getEmail());

//        if(userDetail != null){
//                if(!userDetail.getRole().toString().equals(user.getRole().toString())){
//                    userDetail.setRole(user.getRole());
//                    repository.saveAndFlush(userDetail);
//                }
//        }

//        for (int i = 0; i < userList.size(); i++) {
//            System.out.println(userList.get(i).getEmail());
//            if (userList.get(i).getEmail().equals(user.getEmail())) {
//                System.out.println("Login with Microsoft");
//                login(user, request, response);
//                return ResponseEntity.status(200).body(userDetail);
//            }
//        }

//        System.out.println("Register with Microsoft");
//        if(user.getRole().toString().equals("guest")){
//            user.setRole(Role.guest);
//        }

//        User detail = modelMapper.map(user, User.class);
//        repository.saveAndFlush(detail);
//        login(user, request, response);

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        String token = authorizationHeader.substring("Bearer ".length());
        String[] chunks = token.split("\\.");

        JSONObject header = new JSONObject(decode(chunks[0]));
        JSONObject payload = new JSONObject(decode(chunks[1]));

        String name = payload.getString("name");
        String email = payload.getString("preferred_username");
        String role;
        try {
            role = payload.getJSONArray("roles").getString(0);
        } catch (Exception e) {
            role = "guest";
        }

        User user = repository.findByEmail(email);
        if (user != null) {
            return ResponseEntity.status(200).body(user);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("email", email);
            map.put("role", role);
            return ResponseEntity.status(200).body(map);
        }
    }

    //AUTHENTICATION
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if (user == null) {
            System.out.println("Email not found in the database: " + email);
            throw new UsernameNotFoundException("Email not found in the database: " + email);
        } else {
            System.out.println("Email found in the database: " + email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public Cookie createCookie(String name, String value, Integer maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        return cookie;
    }

    public void login(UserPostMSDTO user, HttpServletRequest request, HttpServletResponse response) {
        String secret = "secret";
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        List<Object> roles = new ArrayList<>();
        roles.add(user.getRole().toString());

        Integer jwtExpirationInMs = 30 * 60 * 1000;
        String access_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .withIssuer(request.getRequestURI())
                .withClaim("roles", roles)
                .sign(algorithm);

        Integer refreshExpirationDateInMs = 24 * 60 * 60 * 1000;
        String refresh_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .withIssuer(request.getRequestURI().toString())
                .sign(algorithm);

        response.addCookie(createCookie("access_token", access_token, jwtExpirationInMs));
        response.addCookie(createCookie("refresh_token", refresh_token, refreshExpirationDateInMs));
    }

    public String decode(String chunks) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String decode = new String(decoder.decode(chunks));
        return decode;
    }

    public String getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        return role;
    }

    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);
        String email = authentication.getPrincipal().toString();
        return email;
    }
}
