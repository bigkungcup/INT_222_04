package int221.kw4.clinics.controllers;

import int221.kw4.clinics.dtos.EventCategoryDTO;
import int221.kw4.clinics.dtos.EventCategoryEditDTO;
import int221.kw4.clinics.dtos.EventCategorypostDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.services.EventCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/eventCategory")
public class EventCategoryController {

    @Autowired
    private EventCategoryService service;

    @GetMapping("")
    public List<EventCategoryDTO> getAllEventCategory() {
        return service.getAll();
    }

    @GetMapping("/{eventCategortId}")
    public EventCategoryDTO getById(@PathVariable Integer eventCategortId){
        return service.getById(eventCategortId);
    }

    @PostMapping("")
    public EventCategory create(@Valid @RequestBody EventCategorypostDTO newEventCategory) {
        return service.addCategory(newEventCategory);
    }

    @DeleteMapping("/{eventCategoryId}")
    public void delete(@PathVariable Integer eventCategoryId){
        service.deleteEvent(eventCategoryId);
    }

    @PutMapping("/{eventCategoryId}")
    public ResponseEntity update(@Valid @RequestBody EventCategoryEditDTO updateEventCategory, @PathVariable Integer eventCategoryId){
        return service.update(updateEventCategory, eventCategoryId);
    }
}
