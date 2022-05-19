package int221.kw4.clinics.services;

import int221.kw4.clinics.dtos.EventDTO;

import int221.kw4.clinics.dtos.EventPageDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.repositories.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
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

    public EventPageDTO getAllEvent(String sortBy, Integer page, Integer pageSize) {
        return modelMapper.map(repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy))), EventPageDTO.class);
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

    private Event mapEvent(Event existingEvent, Event updateEvent) {
        existingEvent.setEventStartTime(updateEvent.getEventStartTime());
        existingEvent.setEventNotes(updateEvent.getEventNotes());
        return existingEvent;
    }

    public Event update(Event updateEvent, Integer eventId) {
        Event event = repository.findById(eventId).map(updateEvents -> mapEvent(updateEvents, updateEvent))
                .orElseGet(() ->
                {
                    updateEvent.setId(eventId);
                    return updateEvent;
                });
        return repository.saveAndFlush(event);
    }

    public List<EventDTO> getPastEvent(Instant instant, Integer page, Integer pageSize) {
        List<Event> pastEvent = repository.findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(instant, PageRequest.of(page, pageSize));
        return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
    }

    public List<EventDTO> getUpcomingEvent(Instant instant, Integer page, Integer pageSize) {
        List<Event> pastEvent = repository.findAllByEventStartTimeAfterOrderByEventStartTimeAsc(instant, PageRequest.of(page, pageSize));
        return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
    }

//    public List<EventDTO> getEventByCategory

}
