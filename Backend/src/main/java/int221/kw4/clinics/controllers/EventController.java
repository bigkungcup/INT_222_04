package int221.kw4.clinics.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import int221.kw4.clinics.advices.HandleExceptionBadRequest;
import int221.kw4.clinics.advices.HandleExceptionForbidden;
import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionOverlap;
import int221.kw4.clinics.dtos.events.EventDTO;
import int221.kw4.clinics.dtos.events.EventEditDTO;
import int221.kw4.clinics.dtos.events.EventPageDTO;
import int221.kw4.clinics.dtos.events.EventPostDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.List;


@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService service;
    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("")
//    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventPageDTO getAllEvent(
            @RequestParam(defaultValue = "eventStartTime") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) throws HandleExceptionNotFound {
        return service.getAllEventPage(sortBy, page, pageSize);
    }

    @GetMapping("/lecturer/{userId}")
    public EventPageDTO getEventByCategories(@PathVariable Integer userId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "6") int pageSize) throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.getEventByCategoryId(userId, page, pageSize);
    }

    @GetMapping("/eventAll")
    public List<EventDTO> getAll() {
        return service.getAllEvent();
    }

    @GetMapping("/{eventId}")
    public EventDTO getEventById(@PathVariable Integer eventId) throws HandleExceptionNotFound, HandleExceptionForbidden, IOException {
        return service.getEventById(eventId);
    }

    @GetMapping("/eventByCategory/{eventCategoryId}")
    public EventPageDTO getAllEventByCategories(
            @PathVariable EventCategory eventCategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.getEventByCategoryId(eventCategoryId, page, pageSize);
    }

    @GetMapping("/pastEvents")
    public EventPageDTO getAllEventByPast(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) throws HandleExceptionNotFound {
        return service.getPastEvent(Instant.now(), page, pageSize);
    }

    @GetMapping("/upComingEvents")
    public EventPageDTO getAllEventByUpComing(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize
    ) throws HandleExceptionNotFound {
        return service.getUpcomingEvent(Instant.now(), page, pageSize);
    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestPart("event") String newEvent, @RequestPart(value = "file", required = false) MultipartFile file,
                                 ServletWebRequest request) throws HandleExceptionOverlap, HandleExceptionBadRequest, JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        EventPostDTO eventPost = objectMapper.readValue(newEvent, EventPostDTO.class);
        return service.addEvent(eventPost, file, request);
    }

    @PostMapping(value = "/guest")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity guestCreate(@Valid @RequestPart("event") String newEvent, @RequestPart(value = "file", required = false) MultipartFile file,
                                      ServletWebRequest request) throws HandleExceptionOverlap, HandleExceptionBadRequest, JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        EventPostDTO eventPost = objectMapper.readValue(newEvent, EventPostDTO.class);
        return service.addEvent(eventPost,file, request);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity delete(@PathVariable Integer eventId) throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.deleteEvent(eventId);
    }

    @PutMapping(value = "/{eventId}",  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity update(@Valid @RequestPart("event") String updateEvent, @PathVariable Integer eventId,
                                 @RequestPart(value = "file", required = false) MultipartFile file, ServletWebRequest request)
            throws HandleExceptionOverlap, HandleExceptionForbidden, HandleExceptionBadRequest, IOException {
        objectMapper.registerModule(new JavaTimeModule());
        EventEditDTO eventEdit = objectMapper.readValue(updateEvent, EventEditDTO.class);
        return service.update(eventEdit, eventId, file, request);
    }
}
