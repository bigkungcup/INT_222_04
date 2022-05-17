package int221.kw4.clinics.controllers;

import int221.kw4.clinics.dtos.EventCategoryDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.services.EventCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("")
    public EventCategory create(@RequestBody EventCategory newEventCategory) {
        return service.addCategory(newEventCategory);
    }

    @DeleteMapping("/{eventCategoryId}")
    public void delete(@PathVariable Integer eventCategoryId){
        service.deleteEvent(eventCategoryId);
    }

    @PutMapping("/{eventCategoryId}")
    public EventCategory update(@RequestBody EventCategory updateEventCategory, @PathVariable Integer eventCategoryId){
        return service.update(updateEventCategory, eventCategoryId);
    }
}
