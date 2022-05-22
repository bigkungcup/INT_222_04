package int221.kw4.clinics.repositories;

import int221.kw4.clinics.dtos.EventCategoryDTO;
import int221.kw4.clinics.dtos.EventDTO;
import int221.kw4.clinics.dtos.EventPageDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    public Page<Event> findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(Instant instantTime, Pageable pageable);

    public Page<Event> findAllByEventStartTimeAfterOrderByEventStartTimeAsc(Instant instantTime, Pageable pageable);

    public Page<Event> findAllByEventCategoryOrderByEventCategoryDesc(EventCategory eventCategory, Pageable pageable);
}
