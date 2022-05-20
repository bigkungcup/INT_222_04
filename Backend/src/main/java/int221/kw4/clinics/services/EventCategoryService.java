package int221.kw4.clinics.services;

import int221.kw4.clinics.dtos.EventCategoryDTO;
import int221.kw4.clinics.dtos.EventCategoryEditDTO;
import int221.kw4.clinics.dtos.EventCategorypostDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.repositories.EventCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventCategoryService {

    private final EventCategoryRepository repository;

    public EventCategoryService(EventCategoryRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    public List<EventCategoryDTO> getAll() {
        List<EventCategory> eventCategoryList = repository.findAll();
        return listMapper.mapList(eventCategoryList, EventCategoryDTO.class, modelMapper);
    }

    public EventCategory addCategory(EventCategorypostDTO newEventCategory) {
        EventCategory eventCategory = modelMapper.map(newEventCategory, EventCategory.class);
        return repository.saveAndFlush(eventCategory);
    }

    public void deleteEvent(Integer eventCategoryId) {
        repository.findById(eventCategoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Event ID: " + eventCategoryId + " does not exist !!!"));
        repository.deleteById(eventCategoryId);
    }

    public ResponseEntity update(EventCategoryEditDTO updateEventCategory, Integer eventCategoryId){
        EventCategory eventCategory = repository.findById(eventCategoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
        modelMapper.map(updateEventCategory, eventCategory);
        return ResponseEntity.status(HttpStatus.OK).body(eventCategory);
    }
}
