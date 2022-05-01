package int221.kw4.clinics.repositories;

import int221.kw4.clinics.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer>{
}
