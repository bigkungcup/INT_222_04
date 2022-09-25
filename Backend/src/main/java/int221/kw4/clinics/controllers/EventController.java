package int221.kw4.clinics.controllers;

//import int221.kw4.clinics.advices.HandleExceptionBadRequest;
//import int221.kw4.clinics.advices.HandleExceptionForbidden;
import int221.kw4.clinics.advices.HandleExceptionBadRequest;
import int221.kw4.clinics.advices.HandleExceptionForbidden;
import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionOverlap;
import int221.kw4.clinics.dtos.*;
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
    ) throws HandleExceptionNotFound {
        return service.getAllEventPage(sortBy, page, pageSize);
    }

    @GetMapping("/eventAll")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public List<EventDTO> getAll() {
        return service.getAllEvent();
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventDTO getEventById(@PathVariable Integer eventId) throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.getEventById(eventId);
    }

    @GetMapping("/eventByCategory/{eventCategoryId}")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventPageDTO getAllEventByCategory(
            @PathVariable EventCategory eventCategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) throws HandleExceptionNotFound {
        return service.getEventByCategoryId(eventCategoryId, page, pageSize);
    }

    @GetMapping("/pastEvents")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventPageDTO getAllEventByPast(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) throws HandleExceptionNotFound {
        return service.getPastEvent(Instant.now(), page, pageSize);
    }

    @GetMapping("/upComingEvents")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventPageDTO getAllEventByUpComing(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) throws HandleExceptionNotFound {
        return service.getUpcomingEvent(Instant.now(), page, pageSize);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public ResponseEntity create(@Valid @RequestBody EventPostDTO newEvent) throws HandleExceptionOverlap, HandleExceptionBadRequest {
        return service.addEvent(newEvent);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public ResponseEntity delete(@PathVariable Integer eventId) throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.deleteEvent(eventId);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public ResponseEntity update(@Valid @RequestBody EventEditDTO updateEvent, @PathVariable Integer eventId) throws HandleExceptionOverlap, HandleExceptionForbidden {
        return service.update(updateEvent, eventId);
    }
}
