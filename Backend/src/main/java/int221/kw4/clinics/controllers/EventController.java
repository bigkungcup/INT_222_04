package int221.kw4.clinics.controllers;

import int221.kw4.clinics.dtos.EventDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService service;

    @GetMapping("")
    public List<EventDTO> getAllEvent() {
        return service.getAllEvent();
    }

    @GetMapping("/{eventId}")
    public EventDTO getEventById(@PathVariable Integer eventId) {
        return service.getEvent(eventId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@RequestBody @Valid Event newEvent) {
        return service.addEvent(newEvent);
    }

    @DeleteMapping("/{eventId}")
    public void delete(@PathVariable Integer eventId){
        service.deleteEvent(eventId);
    }

    @PutMapping("/{eventId}")
    public Event update(@RequestBody Event updateEvent, @PathVariable Integer eventId){
        return service.update(updateEvent, eventId);
    }
}
