package int221.kw4.clinics.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import int221.kw4.clinics.advices.HandleExceptionBadRequest;
import int221.kw4.clinics.advices.HandleExceptionForbidden;
import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionOverlap;
import int221.kw4.clinics.dtos.events.*;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService service;

    @Autowired
    protected Validator validator;
    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("")
//    @PreAuthorize("hasRole('admin')  or hasRole('student') or hasRole('lecturer')")
    public EventPageDTO getAllEvent(@RequestParam(defaultValue = "eventStartTime") String sortBy,
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

    @GetMapping("/filter")
    public EventPageDTO getAllEventByCategories(@RequestParam @Nullable Integer eventCategoryId,
                                                @RequestParam String time,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "6") int pageSize
    ) throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.getEventByEventCategoryAndTime(eventCategoryId, time, page, pageSize);
    }

    @GetMapping("/filterBlind")
    public EventPageBlindDTO getAllEventByCategoriesBlind(@RequestParam @Nullable Integer eventCategoryId,
                                                          @RequestParam String time,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "6") int pageSize
    ) throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.getEventBlind(eventCategoryId, time, page, pageSize);
    }

    @GetMapping("/eventTime")
    public List<EventBlindDTO> getEventByTime(@RequestBody EventTimeDTO eventTime) throws HandleExceptionNotFound, HandleExceptionForbidden {
        String startTime = String.valueOf(eventTime.getEventStartTime());
        Integer eventCategoryId = eventTime.getEventCategoryId();
        return service.getEventByDate(startTime, eventCategoryId);
    }

//    @GetMapping("/eventTime")
//    public List<EventBlindDTO> getEventByTime(@RequestBody @DateTimeFormat(pattern = "yyyy.MM.dd") Date date, @RequestBody Integer eventCategoryId ) throws HandleExceptionNotFound, HandleExceptionForbidden {
//        return service.getEventByDate(date, eventCategoryId);
//    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestPart("event") @Valid EventPostDTO newEvent,
                                 @RequestPart(value = "file") @Nullable  MultipartFile file,
                                 ServletWebRequest request
    ) throws HandleExceptionOverlap, HandleExceptionBadRequest {
        return service.addEvent(newEvent, file, request);
    }

    @PostMapping(value = "/guest")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity guestCreate(@RequestPart("event") @Valid EventPostDTO newEvent,
                                      @RequestPart(value = "file") @Nullable MultipartFile file,
                                      ServletWebRequest request
    ) throws HandleExceptionOverlap, HandleExceptionBadRequest {
        return service.addEvent(newEvent, file, request);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity delete(@PathVariable Integer eventId) throws HandleExceptionNotFound, HandleExceptionForbidden {
        return service.deleteEvent(eventId);
    }

    @PutMapping(value = "/{eventId}")
    public ResponseEntity update(@RequestPart("event") @Valid EventEditDTO updateEvent,
                                 @PathVariable Integer eventId,
                                 @RequestPart(value = "file") @Nullable MultipartFile file,
                                 ServletWebRequest request
    ) throws HandleExceptionOverlap, HandleExceptionForbidden, HandleExceptionBadRequest, IOException, HandleExceptionNotFound {
        return service.update(updateEvent, eventId, file, request);
    }
}
