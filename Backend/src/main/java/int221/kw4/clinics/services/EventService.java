package int221.kw4.clinics.services;

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
                        PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy))),
                EventPageDTO.class);
    }

    public List<EventDTO> getAll(){
        List<Event> eventList = repository.findAll();
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    public EventDTO getEvent(Integer eventId) {
        Event eventListById = repository.findById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Event ID: " + eventId + " does not exist !!!"));
        return modelMapper.map(eventListById, EventDTO.class);
    }

//    public EventPageDTO getEventByCategoryId(EventCategory eventCategoryId, Integer page, Integer pageSize) {
//        return modelMapper.map(repository.findAllByEventCategoryOrderByEventCategoryDesc(
//                        eventCategoryId, PageRequest.of(page, pageSize)),
//                EventPageDTO.class);
//    }

    public List<EventDTO> getEventByCategoryId(EventCategory eventCategoryId, Integer page, Integer pageSize){
        List<Event> eventByCategory =repository.findAllByEventCategoryOrderByEventCategoryDesc(eventCategoryId, PageRequest.of(page, pageSize));
        return listMapper.mapList(eventByCategory, EventDTO.class, modelMapper);
    }


    public List<EventDTO> getPastEvent(Instant instant, Integer page, Integer pageSize) {
        List<Event> pastEvent = repository.findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(instant, PageRequest.of(page, pageSize));
        return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
    }

    public List<EventDTO> getUpcomingEvent(Instant instant, Integer page, Integer pageSize) {
        List<Event> pastEvent = repository.findAllByEventStartTimeAfterOrderByEventStartTimeAsc(instant, PageRequest.of(page, pageSize));
        return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
    }


    //    Post
    public Date findEndDate(Date date, Integer duration) {
        return new Date(date.getTime()+(duration*60000+60000));
    }

    public Event addEvent(EventPostDTO newEvent) {
        Date newEventStartTime = Date.from(newEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(newEvent.getEventStartTime()), newEvent.getEventDuration());
        List<EventDTO> eventList = getAll();

        for (int i = 0; i < eventList.size(); i++) {
            List errors = new ArrayList();
            Date eventStartTime = Date.from(eventList.get(i).getEventStartTime());
            Date eventEndTime = findEndDate(Date.from(eventList.get(i).getEventStartTime()), eventList.get(i).getEventDuration());
            if (newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventStartTime) ||
                    newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventEndTime) ||
                    newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventEndTime) ||
                    newEventStartTime.after(eventStartTime) && newEventEndTime.before(eventEndTime) ||
                    newEventStartTime.equals(eventStartTime))
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Overlap");
            }
        }
        Event event = modelMapper.map(newEvent, Event.class);
        return repository.saveAndFlush(event);
    }

    //    Delete
    public void deleteEvent(Integer eventId) {
        repository.findById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Event ID: " + eventId + " does not exist !!!"));
        repository.deleteById(eventId);
    }

//    Update
    public ResponseEntity update(EventEditDTO updateEvent, Integer eventId) {
        Event event = repository.findById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
        modelMapper.map(updateEvent, event);
        return ResponseEntity.status(200).body(event);
    }

}
