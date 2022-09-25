package int221.kw4.clinics.services;

//import int221.kw4.clinics.advices.HandleExceptionBadRequest;
//import int221.kw4.clinics.advices.HandleExceptionForbidden;
import int221.kw4.clinics.advices.HandleExceptionBadRequest;
import int221.kw4.clinics.advices.HandleExceptionForbidden;
import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionOverlap;
import int221.kw4.clinics.dtos.*;

import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.repositories.EventCategoryRepository;
import int221.kw4.clinics.repositories.EventRepository;
import int221.kw4.clinics.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class EventService {

    private final EventCategoryRepository eventCategoryRepository;
    private final EventRepository repository;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    private final UserRepository userRepository;

    @Autowired
    public EventService(EventRepository repository, EventCategoryRepository eventCategoryRepository, ModelMapper modelMapper, ListMapper listMapper, UserRepository userRepository) {
        this.repository = repository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
        this.userRepository = userRepository;
    }

    //    Get
    public EventPageDTO getAllEventPage(String sortBy, Integer page, Integer pageSize) throws HandleExceptionNotFound {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        System.out.println("User: " + auth.getPrincipal());
        System.out.println("User :" + user);

        if (user.getRole().toString().equals("admin")) {
            return modelMapper.map(repository.findAll(
                    PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, sortBy))), EventPageDTO.class);
        } else if (user.getRole().toString().equals("student")) {
            return modelMapper.map(repository.findAllByBookingEmail(user.getEmail(),
                    PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, sortBy))), EventPageDTO.class);
        }else {
        throw new HandleExceptionNotFound("The event booking email is not the same as student's email");
        }
    }

    public List<EventDTO> getAllEvent() {
        List<Event> eventList = repository.findAll();
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    public EventDTO getEventById(Integer eventId) throws HandleExceptionNotFound, HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        Event event = repository.getById(eventId);
        System.out.println("User: " + auth.getPrincipal());
        System.out.println("User :" + user);

        if(user.getEmail().equals(event.getBookingEmail())){
            Event eventListById = repository.findById(eventId).orElseThrow(
                    () -> new HandleExceptionNotFound(
                            "Event ID: " + eventId + " does not exist !!!"));
            return modelMapper.map(eventListById, EventDTO.class);
        }else {
        throw new HandleExceptionForbidden("The event booking email is not the same as student's email");
        }
    }

    public EventPageDTO getEventByCategoryId(EventCategory eventCategoryId, Integer page, Integer pageSize) throws HandleExceptionNotFound{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        System.out.println("User: " + auth.getPrincipal());
        System.out.println("User :" + user);

        if (user.getRole().toString().equals("admin")) {
            return modelMapper.map(repository.findAllByEventCategory(eventCategoryId,
                    PageRequest.of(page, pageSize)), EventPageDTO.class);
        } else if (user.getRole().toString().equals("student")) {
            return modelMapper.map(repository.findAllByEventCategoryAndBookingEmail(eventCategoryId, user.getEmail(),
                    PageRequest.of(page, pageSize)), EventPageDTO.class);
        }else {
        throw new HandleExceptionNotFound("The event booking email is not the same as student's email");
        }
    }


    public EventPageDTO getPastEvent(Instant instant, Integer page, Integer pageSize) throws HandleExceptionNotFound {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        System.out.println("User1 : " + auth.getPrincipal());
        System.out.println("User2 :" + user.getEmail());

        if (user.getRole().toString().equals("admin")) {
            return modelMapper.map(repository.findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(instant,
                    PageRequest.of(page, pageSize)), EventPageDTO.class);
        } else if (user.getRole().toString().equals("student")) {
            return modelMapper.map(repository.findAllByBookingEmailAndEventStartTimeBeforeOrderByEventStartTimeDesc(user.getEmail(), instant,
                    PageRequest.of(page, pageSize)), EventPageDTO.class);
        }else {
        throw new HandleExceptionNotFound("The event booking email is not the same as student's email");
        }
    }

    public EventPageDTO getUpcomingEvent(Instant instant, Integer page, Integer pageSize) throws HandleExceptionNotFound{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        System.out.println("User: " + auth.getPrincipal());
        System.out.println("User :" + user);

        if (user.getRole().toString().equals("admin")) {
            return modelMapper.map(repository.findAllByEventStartTimeAfterOrderByEventStartTimeAsc(instant,
                            PageRequest.of(page, pageSize)),
                    EventPageDTO.class);
        } else if (user.getRole().toString().equals("student")) {
            return modelMapper.map(repository.findAllByBookingEmailAndEventStartTimeAfterOrderByEventStartTimeAsc(user.getEmail(), instant,
                            PageRequest.of(page, pageSize)),
                    EventPageDTO.class);
        }else {
        throw new HandleExceptionNotFound("The event booking email is not the same as student's email");
        }
    }

    public List<EventDTO> getEventByCurrentTime(Instant instantTime, Integer eventCategoryId) {
        List<Event> eventList = repository.getEventByCurrentTime(instantTime, eventCategoryId);
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    public Event mapEvent(EventPostDTO newEvent) {
        Event event = new Event();
        event.setBookingName(newEvent.getBookingName());
        event.setBookingEmail(newEvent.getBookingEmail());
        event.setEventStartTime(newEvent.getEventStartTime());
        event.setEventNotes(newEvent.getEventNotes());
        event.setEventDuration(newEvent.getEventDuration());
        event.setEventCategory(eventCategoryRepository.findById(newEvent.getEventCategoryId()).get());
        return event;
    }

    //    Post
    public Date findEndDate(Date date, Integer duration) {
        return new Date(date.getTime() + (duration * 60000));
    }

    public ResponseEntity addEvent(EventPostDTO newEvent) throws HandleExceptionOverlap, HandleExceptionBadRequest {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        System.out.println("User: " + auth.getPrincipal());
        System.out.println("User :" + user);

        Date newEventStartTime = Date.from(newEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(newEvent.getEventStartTime()), newEvent.getEventDuration());
        List<EventDTO> eventList = getEventByCurrentTime(newEvent.getEventStartTime(), newEvent.getEventCategoryId());

        if(user.getEmail().equals(newEvent.getBookingEmail())){
            for (int i = 0; i < eventList.size(); i++) {
                Date eventStartTime = Date.from(eventList.get(i).getEventStartTime());
                Date eventEndTime = findEndDate(Date.from(eventList.get(i).getEventStartTime()), eventList.get(i).getEventDuration());
                if (newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventStartTime) ||
                        newEventStartTime.equals(eventStartTime) || newEventStartTime.equals(eventEndTime) ||
                        newEventEndTime.equals(eventStartTime)) {
                    throw new HandleExceptionOverlap("StartTime is Overlap");
                }
            }
            Event event = mapEvent(newEvent);
            repository.saveAndFlush(event);
            return ResponseEntity.status(201).body(event);
        }else {
        throw new HandleExceptionBadRequest("The booking email must be the same as the student's email");
        }
    }


    //    Delete
    public ResponseEntity deleteEvent(Integer eventId) throws HandleExceptionNotFound, HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        Event event = repository.getById(eventId);
        System.out.println("User : " + auth.getPrincipal());
        System.out.println("User : " + user.getEmail() + "       " + event.getBookingEmail());

        if(user.getEmail().equals(event.getBookingEmail())){
            repository.findById(eventId).orElseThrow(
                    () -> new HandleExceptionNotFound(
                            "Event ID: " + eventId + " does not exist !!!")
            );
            repository.deleteById(eventId);
            return ResponseEntity.status(200).body("Delete Event: " + eventId);
        }else {
        throw new HandleExceptionForbidden("The event booking email is not the same as student's email");
        }
    }

    //    Update
    public ResponseEntity update(EventEditDTO updateEvent, Integer eventId) throws HandleExceptionOverlap, HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        Event eventById = repository.getById(eventId);
        System.out.println("User : " + auth.getPrincipal());
        System.out.println("User :" + user.getId() + "   " + eventId);

        Date newEventStartTime = Date.from(updateEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(updateEvent.getEventStartTime()), updateEvent.getEventDuration());
        List<EventDTO> eventList = getAllEvent();

        if(user.getEmail().equals(eventById.getBookingEmail())){
            for (int i = 0; i < eventList.size(); i++) {
                List errors = new ArrayList();
                if (updateEvent.getEventCategory().getId() == eventList.get(i).getEventCategory().getId() && eventList.get(i).getId() != eventId) {
                    Date eventStartTime = Date.from(eventList.get(i).getEventStartTime());
                    Date eventEndTime = findEndDate(Date.from(eventList.get(i).getEventStartTime()), eventList.get(i).getEventDuration());
                    if (newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventStartTime) ||
                            newEventStartTime.equals(eventStartTime) || newEventStartTime.equals(eventEndTime) ||
                            newEventEndTime.equals(eventStartTime)) {
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
        }else {
        throw new HandleExceptionForbidden("The event booking email is not the same as student's email");
        }
    }

}
