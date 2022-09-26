package int221.kw4.clinics.repositories;

import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByBookingEmailAndEventStartTimeBeforeOrderByEventStartTimeDesc(String email, Instant instantTime, Pageable pageable);

    Page<Event> findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(Instant instantTime, Pageable pageable);

    Page<Event> findAllByBookingEmailAndEventStartTimeAfterOrderByEventStartTimeAsc(String email, Instant instantTime, Pageable pageable);

    Page<Event> findAllByEventStartTimeAfterOrderByEventStartTimeAsc(Instant instantTime, Pageable pageable);

    Page<Event> findAllByEventCategoryAndBookingEmail(EventCategory eventCategory, String email, Pageable pageable);

    Page<Event> findAllByEventCategory(EventCategory eventCategory, Pageable pageable);

    Page<Event> findAllByBookingEmail(String email, Pageable pageable);

    Event findByIdAndAndBookingEmail(Integer eventId, String email);

//    @Query(value = "SELECT * FROM event WHERE eventStartTime = :eventStartTime and bookingEmail = :bookingEmail ORDER BY")
    @Query(value = "SELECT * FROM event WHERE cast(eventStartTime as date) = cast(:currentTime as date) AND eventCategoryId = :eventCategoryId", nativeQuery = true)
    List<Event> getEventByCurrentTime(@Param("currentTime") Instant currentTime, @Param("eventCategoryId") Integer eventCategoryId);
}
