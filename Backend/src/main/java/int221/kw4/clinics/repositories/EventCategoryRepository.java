package int221.kw4.clinics.repositories;

import int221.kw4.clinics.entities.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
    EventCategory findByEventCategoryName(String eventCategoryName);
}
