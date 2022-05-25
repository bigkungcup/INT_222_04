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
    public EventPageDTO getAllEvent(
            @RequestParam(defaultValue = "eventStartTime") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) {
        return service.getAllEvent(sortBy, page, pageSize);
    }

    @GetMapping("/eventAllByTime/{eventCategoryId}/{instant}")
    public List<EventDTO> getEventByCurrentTime(@PathVariable Integer eventCategoryId,
                                                @PathVariable Instant instant){
        return service.getEventByCurrentTime(instant, eventCategoryId);
    }

    @GetMapping("/{eventId}")
    public EventDTO getEventById(@PathVariable Integer eventId) throws HandleExceptionNotFound {
        return service.getEvent(eventId);
    }

    @GetMapping("{eventCategoryId}/eventByCategory")
    public  EventPageDTO getAllEventByCategory(
            @PathVariable EventCategory eventCategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ){
        return service.getEventByCategoryId(eventCategoryId, page, pageSize);
    }

    @GetMapping("/pastEvents")
    public EventPageDTO getAllEventByPast(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) {
        return service.getPastEvent(Instant.now(), page, pageSize);
    }

    @GetMapping("/upComingEvents")
    public EventPageDTO getAllEventByUpComing(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) {
        return service.getUpcomingEvent(Instant.now(), page, pageSize);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@Valid @RequestBody EventPostDTO newEvent) throws HandleExceptionOverlap {
        return service.addEvent(newEvent);
    }

    @DeleteMapping("/{eventId}")
    public void delete(@PathVariable Integer eventId) throws HandleExceptionNotFound {
        service.deleteEvent(eventId);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity update(@Valid @RequestBody EventEditDTO updateEvent, @PathVariable Integer eventId) throws HandleExceptionOverlap {
        return service.update(updateEvent, eventId);
    }
}
