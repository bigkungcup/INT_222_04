package int221.kw4.clinics.services;

import java.util.ArrayList;


import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public JwtUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = repository.findByEmail(email);

        if (user == null) {
            try {
                throw new HandleExceptionNotFound("User not found with email: " + email);
            } catch (HandleExceptionNotFound e) {
                throw new RuntimeException(e);
            }
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }
}