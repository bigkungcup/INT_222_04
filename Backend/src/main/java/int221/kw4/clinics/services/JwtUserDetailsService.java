package int221.kw4.clinics.services;

import java.util.Arrays;
import java.util.List;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.dtos.LoginDTO;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService service;
    private final UserRepository repository;

    public JwtUserDetailsService(UserService service, UserRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles = null;

        User user = repository.findByEmail(email);
        if (user == null) {
            try {
                throw new HandleExceptionNotFound("User not found with email: " + email);
            } catch (HandleExceptionNotFound e) {
                throw new RuntimeException(e);
            }
        }
        roles = Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
    }
}