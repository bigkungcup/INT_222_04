package int221.kw4.clinics.controllers;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.HandleExceptionUnique;
import int221.kw4.clinics.dtos.EventCategoryDTO;
import int221.kw4.clinics.dtos.EventCategoryEditDTO;
import int221.kw4.clinics.dtos.EventCategoryPostDTO;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.services.EventCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/eventCategories")
public class EventCategoryController {

    @Autowired
    private EventCategoryService service;

    @GetMapping("")
    public List<EventCategoryDTO> getAllEventCategory() {
        return service.getAll();
    }

    @GetMapping("/{eventCategoryId}")
    public EventCategoryDTO getById(@PathVariable Integer eventCategoryId) throws HandleExceptionNotFound {
        return service.getById(eventCategoryId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public EventCategory create(@Valid @RequestBody EventCategoryPostDTO newEventCategory) {
        return service.addCategory(newEventCategory);
    }

    @DeleteMapping("/{eventCategoryId}")
    public void delete(@PathVariable Integer eventCategoryId) throws HandleExceptionNotFound {
        service.deleteEvent(eventCategoryId);
    }

    @PutMapping("/{eventCategoryId}")
    public ResponseEntity update(@Valid @RequestBody EventCategoryEditDTO updateEventCategory,
                                 @PathVariable Integer eventCategoryId) throws HandleExceptionNotFound, HandleExceptionUnique {
        return service.update(updateEventCategory, eventCategoryId);
    }
}
