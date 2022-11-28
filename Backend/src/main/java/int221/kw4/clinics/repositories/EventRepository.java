package int221.kw4.clinics.repositories;

import int221.kw4.clinics.dtos.events.EventBlindDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByBookingEmailAndEventStartTimeBeforeOrderByEventStartTimeDesc(String email, Instant instantTime, Pageable pageable);

    Page<Event> findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(Instant instantTime, Pageable pageable);

    Page<Event> findAllByBookingEmailAndEventStartTimeAfterOrderByEventStartTimeAsc(String email, Instant instantTime, Pageable pageable);

    Page<Event> findAllByEventStartTimeAfterOrderByEventStartTimeAsc(Instant instantTime, Pageable pageable);

    Page<Event> findAllByEventCategoryAndBookingEmail(EventCategory eventCategory, String email, Pageable pageable);

    Page<Event> findAllByEventCategory(EventCategory eventCategory, Pageable pageable);

    Page<Event> findAllByEventCategory_Id(Integer eventCategoryId, Pageable pageable);

    Page<Event> findAllByBookingEmailAndEventCategory_Id(String email, Integer eventCategoryId, Pageable pageable);

    Page<Event> findAllByBookingEmail(String email, Pageable pageable);

    Event findByIdAndBookingEmail(Integer eventId, String email);

    Page<Event> findByEventCategory_IdIn(Collection<Integer> ids, Pageable pageable);

    Page<Event> findAllByEventCategory_IdIn(Collection<Integer> ids, Pageable pageable);

    Page<Event> findAllByEventCategory_IdAndEventStartTimeBeforeOrderByEventStartTimeDesc(Integer ids, Instant instantTime, Pageable pageable);

    Page<Event> findAllByEventCategory_IdAndEventStartTimeAfterOrderByEventStartTimeAsc(Integer ids, Instant instantTime, Pageable pageable);

    Page<Event> findAllByBookingEmailAndEventCategory_IdAndEventStartTimeBeforeOrderByEventStartTimeDesc(String email, Integer ids, Instant instantTime, Pageable pageable);

    Page<Event> findAllByEventCategory_IdInAndEventStartTimeBeforeOrderByEventStartTimeDesc(Collection<Integer> ids, Instant instantTime, Pageable pageable);

    Page<Event> findAllByBookingEmailAndEventCategory_IdAndEventStartTimeAfterOrderByEventStartTimeAsc(String email, Integer ids, Instant instantTime, Pageable pageable);

    Page<Event> findAllByEventCategory_IdInAndEventStartTimeAfterOrderByEventStartTimeAsc(Collection<Integer> ids, Instant instantTime, Pageable pageable);

    Page<Event> findAll(Pageable pageable);

    List<Event> findAllByEventStartTimeContainingAndEventCategory_Id(String date, Integer id);

    @Query(value = "SELECT * FROM event WHERE cast(eventStartTime as varchar) LIKE %:date% AND eventCategoryId = :id", nativeQuery = true)
    List<Event> findAllByEventStartTimeContainingAndEventCategory_IdNative(@Param("date") String date, @Param("id") Integer id);

    @Query(value = "SELECT * FROM event WHERE cast(eventStartTime as date) = cast(:currentTime as date) AND eventCategoryId = :eventCategoryId", nativeQuery = true)
    List<Event> getEventByCurrentTime(@Param("currentTime") Instant currentTime, @Param("eventCategoryId") Integer eventCategoryId);
}
