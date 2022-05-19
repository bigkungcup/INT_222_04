package int221.kw4.clinics.controllers;

import int221.kw4.clinics.dtos.EventDTO;
import int221.kw4.clinics.dtos.EventPageDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{eventId}")
    public EventDTO getEventById(@PathVariable Integer eventId) {
        return service.getEvent(eventId);
    }


    @GetMapping("/pastEvent")
    public List<EventDTO> getAllBookingByPast(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) {
        return service.getPastEvent(Instant.now(), page, pageSize);
    }

    @GetMapping("/upComingEvent")
    public List<EventDTO> getAllBookingByUpComing(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) {
        return service.getUpcomingEvent(Instant.now(), page, pageSize);
    }


    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@RequestBody @Valid Event newEvent) {
        return service.addEvent(newEvent);
    }

    @DeleteMapping("/{eventId}")
    public void delete(@PathVariable Integer eventId) {
        service.deleteEvent(eventId);
    }

    @PutMapping("/{eventId}")
    public Event update(@RequestBody Event updateEvent, @PathVariable Integer eventId) {
        return service.update(updateEvent, eventId);
    }
}
