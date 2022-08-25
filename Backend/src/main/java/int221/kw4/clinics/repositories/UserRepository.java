package int221.kw4.clinics.repositories;

import int221.kw4.clinics.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);
}

