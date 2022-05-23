package int221.kw4.clinics.services;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionOverlap;
import int221.kw4.clinics.dtos.*;

import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.repositories.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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

    //    Get
    public EventPageDTO getAllEvent(String sortBy, Integer page, Integer pageSize) {
        return modelMapper.map(repository.findAll(
                        PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, sortBy))),
                EventPageDTO.class);
    }

    public List<EventDTO> getAll() {
        List<Event> eventList = repository.findAll();
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    public EventDTO getEvent(Integer eventId) throws HandleExceptionNotFound {
        Event eventListById = repository.findById(eventId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "Event ID: " + eventId + " does not exist !!!"));
        return modelMapper.map(eventListById, EventDTO.class);
    }

    public EventPageDTO getEventByCategoryId(EventCategory eventCategoryId, Integer page, Integer pageSize) {
        return modelMapper.map(repository.findAllByEventCategory(eventCategoryId, PageRequest.of(page, pageSize)),
                EventPageDTO.class);
    }


    public EventPageDTO getPastEvent(Instant instant, Integer page, Integer pageSize) {
        return modelMapper.map(repository.findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(instant, PageRequest.of(page, pageSize)),
                EventPageDTO.class);
    }

    public EventPageDTO getUpcomingEvent(Instant instant, Integer page, Integer pageSize) {
        return modelMapper.map(repository.findAllByEventStartTimeAfterOrderByEventStartTimeAsc(instant, PageRequest.of(page, pageSize)),
                EventPageDTO.class);
    }


    //    Post
    public Date findEndDate(Date date, Integer duration) {
        return new Date(date.getTime() + (duration * 60000 + 60000));
    }

    public Event addEvent(EventPostDTO newEvent) throws HandleExceptionOverlap {
        Date newEventStartTime = Date.from(newEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(newEvent.getEventStartTime()), newEvent.getEventDuration());
        List<EventDTO> eventList = getAll();

        for (int i = 0; i < eventList.size(); i++) {
            List errors = new ArrayList();
            if (newEvent.getEventCategory().getId() == eventList.get(i).getEventCategory().getId()) {
                Date eventStartTime = Date.from(eventList.get(i).getEventStartTime());
                Date eventEndTime = findEndDate(Date.from(eventList.get(i).getEventStartTime()), eventList.get(i).getEventDuration());
                if (newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventStartTime) ||
                        newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventEndTime) ||
                        newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventEndTime) ||
                        newEventStartTime.after(eventStartTime) && newEventEndTime.before(eventEndTime) ||
                        newEventStartTime.equals(eventStartTime)) {
                    throw new HandleExceptionOverlap(errors.toString());
                }
            }
        }
        Event event = modelMapper.map(newEvent, Event.class);
        return repository.saveAndFlush(event);
    }

    //    Delete
    public void deleteEvent(Integer eventId) throws HandleExceptionNotFound {
        repository.findById(eventId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "Event ID: " + eventId + " does not exist !!!")
        );
        repository.deleteById(eventId);
    }

    //    Update
    public ResponseEntity update(EventEditDTO updateEvent, Integer eventId) throws HandleExceptionOverlap {
        Date newEventStartTime = Date.from(updateEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(updateEvent.getEventStartTime()), updateEvent.getEventDuration());
        List<EventDTO> eventList = getAll();

        for (int i = 0; i < eventList.size(); i++) {
            List errors = new ArrayList();
            if (updateEvent.getEventCategory().getId() == eventList.get(i).getEventCategory().getId() && eventList.get(i).getId() != eventId) {
                Date eventStartTime = Date.from(eventList.get(i).getEventStartTime());
                Date eventEndTime = findEndDate(Date.from(eventList.get(i).getEventStartTime()), eventList.get(i).getEventDuration());
                if (newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventStartTime) ||
                        newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventEndTime) ||
                        newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventEndTime) ||
                        newEventStartTime.after(eventStartTime) && newEventEndTime.before(eventEndTime) ||
                        newEventStartTime.equals(eventStartTime)) {
                    throw new HandleExceptionOverlap(errors.toString());
                }
            }
        }

        Event event = repository.findById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
        modelMapper.map(updateEvent, event);
        repository.saveAndFlush(event);
        return ResponseEntity.status(200).body(event);
    }

}
