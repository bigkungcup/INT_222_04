package int221.kw4.clinics.repositories;

import int221.kw4.clinics.entities.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
}
