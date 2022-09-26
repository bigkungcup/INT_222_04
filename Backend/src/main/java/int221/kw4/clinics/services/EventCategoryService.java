package int221.kw4.clinics.services;

import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.dtos.eventCategories.EventCategoryDTO;
import int221.kw4.clinics.dtos.eventCategories.EventCategoryEditDTO;
import int221.kw4.clinics.dtos.eventCategories.EventCategoryPostDTO;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.repositories.EventCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCategoryService {

    private final EventCategoryRepository repository;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    public EventCategoryService(EventCategoryRepository repository, ModelMapper modelMapper, ListMapper listMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
    }

    public List<EventCategoryDTO> getAll() {
        List<EventCategory> eventCategoryList = repository.findAll();
        return listMapper.mapList(eventCategoryList, EventCategoryDTO.class, modelMapper);
    }

    public EventCategoryDTO getById(Integer eventCategoryId) throws HandleExceptionNotFound {
        EventCategory eventCategoryList = repository.findById(eventCategoryId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "CategoryId: " + eventCategoryId + " does not exist !!!")
        );
        return modelMapper.map(eventCategoryList, EventCategoryDTO.class);
    }

    public EventCategory addCategory(EventCategoryPostDTO newEventCategory) {
        EventCategory eventCategory = modelMapper.map(newEventCategory, EventCategory.class);
        return repository.saveAndFlush(eventCategory);
    }

    public void deleteEvent(Integer eventCategoryId) throws HandleExceptionNotFound {
        repository.findById(eventCategoryId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "CategoryId: " + eventCategoryId + " does not exist !!!")
        );
        repository.deleteById(eventCategoryId);
    }

    public ResponseEntity update(EventCategoryEditDTO updateEventCategory, Integer eventCategoryId) throws HandleExceptionNotFound {
        EventCategory eventCategory = repository.findById(eventCategoryId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "CategoryId: " + eventCategoryId + " does not exist !!!")
        );
        modelMapper.map(updateEventCategory, eventCategory);
        repository.saveAndFlush(eventCategory);
        return ResponseEntity.status(HttpStatus.OK).body(eventCategory);
    }
}
