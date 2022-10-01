package int221.kw4.clinics.repositories;

import int221.kw4.clinics.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
//    List<User> findUsersByEventCategoriesId(Integer id);
}

