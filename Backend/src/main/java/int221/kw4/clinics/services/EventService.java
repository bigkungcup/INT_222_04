package int221.kw4.clinics.services;

import int221.kw4.clinics.dtos.EventDTO;

import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.repositories.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class EventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    public List<EventDTO> getAllEvent() {
        List<Event> eventList = repository.findAll();
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    public EventDTO getEvent(Integer eventId) {
        Event eventListById = repository.findById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Event ID: " + eventId + " does not exist !!!"));
        return modelMapper.map(eventListById, EventDTO.class);
    }

    public Event addEvent(Event newEvent) {
        return repository.saveAndFlush(newEvent);
    }

    public void deleteEvent(Integer eventId) {
        repository.findById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Event ID: " + eventId + " does not exist !!!"));
        repository.deleteById(eventId);
    }

    private Event mapEvent(Event existingEvent, Event updateEvent){
        existingEvent.setBookingName(updateEvent.getBookingName());
        existingEvent.setBookingEmail(updateEvent.getBookingEmail());
        existingEvent.setEventStartTime(updateEvent.getEventStartTime());
        existingEvent.setEventNotes(updateEvent.getEventNotes());
        existingEvent.setEventDuration(updateEvent.getEventDuration());
        existingEvent.setEventCategory(updateEvent.getEventCategory());
        return  existingEvent;
    }

    public Event update(Event updateEvent, Integer eventId) {
        Event event = repository.findById(eventId).map(o->mapEvent(o, updateEvent))
                .orElseGet(()->
                {
                    updateEvent.setId(eventId);
                    return updateEvent;
                });
        return repository.saveAndFlush(event);
    }
}
