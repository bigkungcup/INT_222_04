package int221.kw4.clinics.controllers;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionOverlap;
import int221.kw4.clinics.dtos.*;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService service;

    @GetMapping("")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventPageDTO getAllEvent(
            @RequestParam(defaultValue = "eventStartTime") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) {
        return service.getAllEvent(sortBy, page, pageSize);
    }

    @GetMapping("/eventAll")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public List<EventDTO> getAll(){
        return service.getAll();
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventDTO getEventById(@PathVariable Integer eventId) throws HandleExceptionNotFound {
        return service.getEvent(eventId);
    }

    @GetMapping("/eventByCategory/{eventCategoryId}")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public  EventPageDTO getAllEventByCategory(
            @PathVariable EventCategory eventCategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ){
        return service.getEventByCategoryId(eventCategoryId, page, pageSize);
    }

    @GetMapping("/pastEvents")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventPageDTO getAllEventByPast(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) {
        return service.getPastEvent(Instant.now(), page, pageSize);
    }

    @GetMapping("/upComingEvents")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventPageDTO getAllEventByUpComing(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) {
        return service.getUpcomingEvent(Instant.now(), page, pageSize);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public ResponseEntity create(@Valid @RequestBody EventPostDTO newEvent) throws HandleExceptionOverlap {
        return service.addEvent(newEvent);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public void delete(@PathVariable Integer eventId) throws HandleExceptionNotFound {
        service.deleteEvent(eventId);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public ResponseEntity update(@Valid @RequestBody EventEditDTO updateEvent, @PathVariable Integer eventId) throws HandleExceptionOverlap {
        return service.update(updateEvent, eventId);
    }
}
