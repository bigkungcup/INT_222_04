package int221.kw4.clinics.services;

import int221.kw4.clinics.dtos.EventDTO;

import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.repositories.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public List<EventDTO> getAllEvent(String time, Integer page,Integer pageSize) {
        List<Event> eventList = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,time))).getContent();
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
        existingEvent.setEventStartTime(updateEvent.getEventStartTime());
        existingEvent.setEventNotes(updateEvent.getEventNotes());
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
