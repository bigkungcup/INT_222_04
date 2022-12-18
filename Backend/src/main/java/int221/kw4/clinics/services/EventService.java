package int221.kw4.clinics.services;

import int221.kw4.clinics.advices.*;

import int221.kw4.clinics.dtos.events.*;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.entities.EventCategory;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.properties.FileStorageProperties;
import int221.kw4.clinics.repositories.EventCategoryRepository;
import int221.kw4.clinics.repositories.EventRepository;
import int221.kw4.clinics.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class EventService {
    private final EventCategoryRepository eventCategoryRepository;
    private final EventRepository repository;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;

    private final FileStorageProperties fileStorageProperties;

    public EventService(EventRepository repository, EventCategoryRepository eventCategoryRepository, ModelMapper modelMapper,
                        ListMapper listMapper, UserRepository userRepository, EmailService emailService, FileStorageService fileStorageService,
                        FileStorageProperties fileStorageProperties) {
        this.repository = repository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
        this.fileStorageProperties = fileStorageProperties;
    }

    //    Get
    public EventPageDTO getAllEventPage(String sortBy, Integer page, Integer pageSize) throws HandleExceptionNotFound {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByEmail(auth.getPrincipal().toString());
//        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();
//        String email = auth.getPrincipal().toString();

        System.out.println("User: " + getEmail());
        System.out.println("Email: " + getRole());

        if (getRole().equals("admin")) {
            return modelMapper.map(repository.findAll(
                    PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, sortBy))), EventPageDTO.class);

        } else if (getRole().equals("student")) {
            return modelMapper.map(repository.findAllByBookingEmail(getEmail(),
                    PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, sortBy))), EventPageDTO.class);
        } else {
            throw new HandleExceptionNotFound("The event booking email is not the same as student's email");
        }
    }

    public List<EventDTO> getAllEvent() {
        List<Event> eventList = repository.findAll();
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    public List<EventBlindDTO> getAllEventBlind() {
        List<Event> eventList = repository.findAll();
        return listMapper.mapList(eventList, EventBlindDTO.class, modelMapper);
    }

    public EventDTO getEventById(Integer eventId) throws HandleExceptionNotFound, HandleExceptionForbidden, IOException {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Event event = repository.getById(eventId);

        String userDir = event.getUser() != null ? "User/" + "User_" + event.getUser().getEmail() : "Guest";
        Path path = Paths.get(fileStorageProperties.getUploadDir() + "/" + userDir + "/" + "Event_" + event.getId());
        System.out.println("Path: " + path);

        System.out.println("User: " + getEmail());
        System.out.println("Email: " + getRole());

        if (getRole().equals("admin")) {
            Event eventListById = repository.findById(eventId).orElseThrow(
                    () -> new HandleExceptionNotFound(
                            "Event ID: " + eventId + " does not exist !!!"));
            EventDTO eventDTO = modelMapper.map(eventListById, EventDTO.class);
            eventDTO.setFileName(getFile(path, eventListById).get("fileName"));
            return eventDTO;
        } else if (getRole().equals("student")) {
            if (getEmail().equals(event.getBookingEmail())) {
                Event eventListById = repository.findByIdAndBookingEmail(eventId, getEmail());
                EventDTO eventDTO = modelMapper.map(eventListById, EventDTO.class);
                eventDTO.setFileName(getFile(path, eventListById).get("fileName"));
                return eventDTO;
            } else {
                throw new HandleExceptionForbidden("The event booking email is not the same as student's email");
            }
        } else if (getRole().equals("lecturer")) {
            User user = userRepository.findByEmail(getEmail());
            if(user != null){
                List<Integer> categoryIds = user.getEventCategories().stream().map(EventCategory::getId).collect(Collectors.toList());
                if (categoryIds.contains(event.getEventCategory().getId())) {
                    Event eventListById = repository.findById(eventId).orElseThrow(
                            () -> new HandleExceptionNotFound(
                                    "Event ID: " + eventId + " does not exist !!!"));
                    EventDTO eventDTO = modelMapper.map(eventListById, EventDTO.class);
                    eventDTO.setFileName(getFile(path, eventListById).get("fileName"));
                    return eventDTO;
                } else {
                    throw new HandleExceptionForbidden("The event category is not the same as lecturer's category");
                }
            }else {
                throw new HandleExceptionForbidden("The event category is not the same as lecturer's category"); //ควรทำอย่างไร
            }

        } else {
            throw new HandleExceptionForbidden("The event booking email is not the same as email");
        }
    }

    public EventPageDTO getEventByCategoryId(Integer userId, Integer page, Integer pageSize) throws HandleExceptionNotFound, HandleExceptionForbidden {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new HandleExceptionNotFound(
                        "User ID: " + userId + " does not exist !!!")
        );

        if (user.getRole().toString().equals("student")) {
            throw new HandleExceptionForbidden("Access denied for user:" + user.getEmail());
        } else {
            List<Integer> categoryIds = user.getEventCategories().stream().map(EventCategory::getId).collect(Collectors.toList());
            System.out.println(categoryIds);
            return modelMapper.map(repository.findByEventCategory_IdIn(categoryIds,
                    PageRequest.of(page, pageSize)), EventPageDTO.class);
        }

    }
    public EventPageDTO getEventByEventCategoryAndTime(Integer eventCategoryId, String time, Integer page, Integer pageSize) throws HandleExceptionNotFound, HandleExceptionForbidden {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByEmail(auth.getPrincipal().toString());

        switch (time) {
            case "past":
                if (getRole().equals("admin")) {
                    if (eventCategoryId != 0) {
                        return modelMapper.map(repository.findAllByEventCategory_IdAndEventStartTimeBeforeOrderByEventStartTimeDesc(eventCategoryId, Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    } else {
                        return modelMapper.map(repository.findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    }
                } else if (getRole().equals("student")) {
                    if (eventCategoryId != 0) {
                        return modelMapper.map(repository.findAllByBookingEmailAndEventCategory_IdAndEventStartTimeBeforeOrderByEventStartTimeDesc(getEmail(), eventCategoryId, Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    } else {
                        return modelMapper.map(repository.findAllByBookingEmailAndEventStartTimeBeforeOrderByEventStartTimeDesc(getEmail(), Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    }
                } else if (getRole().equals("lecturer")) {
                    User user = userRepository.findByEmail(getEmail());
                    if(user != null){
                        List<Integer> categoryIds = user.getEventCategories().stream().map(EventCategory::getId).collect(Collectors.toList());
                        if (eventCategoryId != 0) {
                            if (categoryIds.contains(eventCategoryId)) {
                                return modelMapper.map(repository.findAllByEventCategory_IdAndEventStartTimeBeforeOrderByEventStartTimeDesc(eventCategoryId, Instant.now(),
                                        PageRequest.of(page, pageSize)), EventPageDTO.class);
                            } else {
                                throw new HandleExceptionForbidden("The event category is not the same as lecturer's category");
                            }
                        } else {
                            return modelMapper.map(repository.findAllByEventCategory_IdInAndEventStartTimeBeforeOrderByEventStartTimeDesc(categoryIds, Instant.now(),
                                    PageRequest.of(page, pageSize)), EventPageDTO.class);
                        }
                    }else {
                        throw new HandleExceptionForbidden("The event category is not the same as lecturer's category"); //ควรทำอย่างไร
                    }
                }
                break;
            case "upComing":
                if (getRole().equals("admin")) {
                    if (eventCategoryId != 0) {
                        return modelMapper.map(repository.findAllByEventCategory_IdAndEventStartTimeAfterOrderByEventStartTimeAsc(eventCategoryId, Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    } else {
                        return modelMapper.map(repository.findAllByEventStartTimeAfterOrderByEventStartTimeAsc(Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    }
                } else if (getRole().equals("student")) {
                    if (eventCategoryId != 0) {
                        return modelMapper.map(repository.findAllByBookingEmailAndEventCategory_IdAndEventStartTimeAfterOrderByEventStartTimeAsc(getEmail(), eventCategoryId, Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageDTO.class);

                    } else {
                        return modelMapper.map(repository.findAllByBookingEmailAndEventStartTimeAfterOrderByEventStartTimeAsc(getEmail(), Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    }
                } else if (getRole().equals("lecturer")) {
                    User user = userRepository.findByEmail(getEmail());
                    if(user != null){
                        List<Integer> categoryIds = user.getEventCategories().stream().map(EventCategory::getId).collect(Collectors.toList());
                        if (eventCategoryId != 0) {
                            if (categoryIds.contains(eventCategoryId)) {
                                return modelMapper.map(repository.findAllByEventCategory_IdAndEventStartTimeAfterOrderByEventStartTimeAsc(eventCategoryId, Instant.now(),
                                        PageRequest.of(page, pageSize)), EventPageDTO.class);
                            } else {
                                throw new HandleExceptionForbidden("The event category is not the same as lecturer's category");
                            }
                        } else {
                            return modelMapper.map(repository.findAllByEventCategory_IdInAndEventStartTimeAfterOrderByEventStartTimeAsc(categoryIds, Instant.now(),
                                    PageRequest.of(page, pageSize)), EventPageDTO.class);
                        }
                    }else {
                        throw new HandleExceptionForbidden("The event category is not the same as lecturer's category"); //ควรทำอย่างไร
                    }
                }
                break;
            case "all":
                if (getRole().equals("admin")) {
                    if (eventCategoryId != 0) {
                        return modelMapper.map(repository.findAllByEventCategory_Id(eventCategoryId,
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    } else {
                        return modelMapper.map(repository.findAll(PageRequest.of(page, pageSize)), EventPageDTO.class);
                    }
                } else if (getRole().equals("student")) {
                    if (eventCategoryId != 0) {
                        return modelMapper.map(repository.findAllByBookingEmailAndEventCategory_Id(getEmail(), eventCategoryId,
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    } else {
                        return modelMapper.map(repository.findAllByBookingEmail(getEmail(),
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    }
                } else if (getRole().equals("lecturer")) {
                    User user = userRepository.findByEmail(getEmail());
                    List<Integer> categoryIds = user.getEventCategories().stream().map(EventCategory::getId).collect(Collectors.toList());
                    if (eventCategoryId != 0) {
                        if (categoryIds.contains(eventCategoryId)) {
                            return modelMapper.map(repository.findAllByEventCategory_Id(eventCategoryId,
                                    PageRequest.of(page, pageSize)), EventPageDTO.class);
                        } else {
                            throw new HandleExceptionForbidden("The event category is not the same as lecturer's category");
                        }
                    } else {
                        return modelMapper.map(repository.findAllByEventCategory_IdIn(categoryIds,
                                PageRequest.of(page, pageSize)), EventPageDTO.class);
                    }
                }
                break;
        }
        return null;
    }

    public EventPageBlindDTO getEventBlind(Integer eventCategoryId, String time, Integer page, Integer pageSize) throws HandleExceptionNotFound, HandleExceptionForbidden {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByEmail(getEmail());

        switch (time) {
            case "past":
               if (getRole().equals("admin") || getRole().equals("student") || getRole().equals("lecturer") ||
                       getRole().equals("anonymousUser") || getRole().equals("guest")) {
                    if (eventCategoryId != 0) {
                        return modelMapper.map(repository.findAllByEventCategory_IdAndEventStartTimeBeforeOrderByEventStartTimeDesc(eventCategoryId, Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageBlindDTO.class);
                    } else {
                        return modelMapper.map(repository.findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageBlindDTO.class);
                    }
                }
                break;
            case "upComing":
               if (getRole().equals("admin") || getRole().equals("student") || getRole().equals("lecturer") ||
                       getRole().equals("anonymousUser") || getRole().equals("guest")) {
                    if (eventCategoryId != 0) {
                        return modelMapper.map(repository.findAllByEventCategory_IdAndEventStartTimeAfterOrderByEventStartTimeAsc(eventCategoryId, Instant.now(),
                                PageRequest.of(page, pageSize)),EventPageBlindDTO.class);

                    } else {
                        return modelMapper.map(repository.findAllByEventStartTimeAfterOrderByEventStartTimeAsc(Instant.now(),
                                PageRequest.of(page, pageSize)), EventPageBlindDTO.class);
                    }
                }
                break;
            case "all":
               if (getRole().equals("admin") || getRole().equals("student") || getRole().equals("lecturer") ||
                       getRole().equals("anonymousUser") || getRole().equals("guest")) {
                    if (eventCategoryId != 0) {
                        return modelMapper.map(repository.findAllByEventCategory_Id(eventCategoryId,
                                PageRequest.of(page, pageSize)), EventPageBlindDTO.class);
                    } else {
                        return modelMapper.map(repository.findAll(PageRequest.of(page, pageSize)), EventPageBlindDTO.class);
                    }
                }
                break;
        }
        return null;
    }

    public List<EventBlindDTO> getEventByCurrentTime(Instant instantTime, Integer eventCategoryId) {
        List<Event> eventList = repository.getEventByCurrentTime(instantTime, eventCategoryId);
        return listMapper.mapList(eventList, EventBlindDTO.class, modelMapper);
    }

    public List<EventBlindDTO> getEventByDate(String date, Integer eventCategoryId) {
        List<Event> eventList = repository.findAllByEventStartTimeContainingAndEventCategory_IdNative(date, eventCategoryId);
        return listMapper.mapList(eventList, EventBlindDTO.class, modelMapper);
    }


    //    Post
    public ResponseEntity addEvent(EventPostDTO newEvent, MultipartFile file, ServletWebRequest request) throws HandleExceptionOverlap, HandleExceptionBadRequest {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User1: " + getEmail());
//        System.out.println("New Event: " + newEvent);

        if (!getRole().equals("anonymousUser")) {
            User user = userRepository.findByEmail(getEmail());
            HandleValidationError error = addOverlap(newEvent, request);
            if (error != null) {
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            }
            if (getRole().equals("admin")) {
                newEvent.setUser(user);
                Event event = mapEvent(newEvent);
                repository.saveAndFlush(event);
                fileStorageService.storeFile(file, event);
//                sendEmail(event);
                return ResponseEntity.status(201).body(event);

            } else if (getRole().equals("student")) {
                if (getEmail().equals(newEvent.getBookingEmail())) {
                    newEvent.setUser(user);
                    Event event = mapEvent(newEvent);
                    repository.saveAndFlush(event);
                    fileStorageService.storeFile(file, event);
//                    sendEmail(event);
                    return ResponseEntity.status(201).body(event);
                } else {
                    throw new HandleExceptionBadRequest("The booking email must be the same as the student's email");
                }
            } else if (getRole().equals("guest")) {
                System.out.println("Email: " + getEmail());
                if (getEmail().equals(newEvent.getBookingEmail())) {
                    newEvent.setUser(user);
                    Event event = mapEvent(newEvent);
                    repository.saveAndFlush(event);
                    fileStorageService.storeFile(file, event);
//                    sendEmail(event);
                    return ResponseEntity.status(201).body(event);
                } else {
                    throw new HandleExceptionBadRequest("The booking email must be the same as the student's email");
                }
            } else {
                throw new HandleExceptionBadRequest("The booking email must be the same as the email");
            }
        } else {
            HandleValidationError error = addOverlap(newEvent, request);
            if (error != null) {
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            }
            if(getEmail().equals(newEvent.getBookingEmail())) {
                Event event = mapEvent(newEvent);
                repository.saveAndFlush(event);
                fileStorageService.storeFile(file, event);
//                sendEmail(event);
                return ResponseEntity.status(201).body(event);
            } else {
                throw new HandleExceptionBadRequest("The booking email must be the same as the email");
            }
        }
    }

    //    Delete
    public ResponseEntity deleteEvent(Integer eventId) throws HandleExceptionNotFound, HandleExceptionForbidden {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        Event event = repository.getById(eventId);

        String userDir = event.getUser() != null ? "User/" + "User_" + event.getUser().getId() : "Guest";
        String path = Paths.get(fileStorageProperties.getUploadDir() + "/" + userDir + "/" + "Event_" + event.getId()).toString();

        if (getRole().equals("admin")) {
            repository.findById(eventId).orElseThrow(
                    () -> new HandleExceptionNotFound(
                            "Event ID: " + eventId + " does not exist !!!")
            );
            fileStorageService.deleteDir(path);
            repository.deleteById(eventId);
//            sendEmail(event);
            return ResponseEntity.status(200).body("Delete Event: " + eventId);

        } else if (getRole().equals("student")) {
            if (getEmail().equals(event.getBookingEmail())) {
                repository.findById(eventId).orElseThrow(
                        () -> new HandleExceptionNotFound(
                                "Event ID: " + eventId + " does not exist !!!")
                );
                fileStorageService.deleteDir(path);
                repository.deleteById(eventId);
//                sendEmail(event);
                return ResponseEntity.status(200).body("Delete Event: " + eventId);

            } else {
                throw new HandleExceptionForbidden("The event booking email is not the same as student's email");
            }
        } else {
            throw new HandleExceptionForbidden("The event booking email is not the same as email");
        }
    }

    //    Update
    public ResponseEntity update(EventEditDTO updateEvent, Integer eventId, MultipartFile file, ServletWebRequest request) throws HandleExceptionOverlap, HandleExceptionForbidden, HandleExceptionBadRequest, IOException, HandleExceptionNotFound {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByEmail(auth.getPrincipal().toString());
        Event eventById = repository.getById(eventId);

        String userDir = eventById.getUser() != null ? "User/" + "User_" + eventById.getUser().getId() : "Guest";
        Path path = Paths.get(fileStorageProperties.getUploadDir() + "/" + userDir + "/" + "Event_" + eventById.getId());

        if (getRole().equals("admin")) {
            HandleValidationError error = updateOverlap(updateEvent, eventId, request);
            if (error != null) {
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            }
            updateFile(file, path, eventById);
            Event event = repository.findById(eventId).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
            );
            modelMapper.map(updateEvent, event);
            repository.saveAndFlush(event);
//            sendEmail(event);
            return ResponseEntity.status(200).body(event);
        } else if (getRole().equals("student")) {
            HandleValidationError error = updateOverlap(updateEvent, eventId, request);
            if (error != null) {
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            }
            if (getEmail().equals(eventById.getBookingEmail())) {
                updateOverlap(updateEvent, eventId, request);
                updateFile(file, path, eventById);
                Event event = repository.findById(eventId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
                );
                event.setEventCategory(updateEvent.getEventCategory());
                modelMapper.map(updateEvent, event);
                repository.saveAndFlush(event);
//                sendEmail(event);
                return ResponseEntity.status(200).body(event);
            } else {
                throw new HandleExceptionForbidden("The booking email must be the same as the student's email");
            }
        } else {
            throw new HandleExceptionForbidden("The event booking email is not the same as email");
        }
    }

    public void updateFile(MultipartFile file, Path path, Event eventById) throws IOException, HandleExceptionNotFound {
        if (file != null) {
            if (Files.exists(path)) {
                if (!Files.list(path).collect(Collectors.toList()).isEmpty()) {
                    Path pathFile = Files.list(path).collect(Collectors.toList()).get(0);
                    String fileName = fileStorageService.loadFileAsResource(pathFile.toString(), eventById).getFilename();
                    if(Objects.equals(fileName, file.getOriginalFilename())) {
                        fileStorageService.deleteFile(path + "/" + Files.list(path).collect(Collectors.toList()).get(0).getFileName());
                        fileStorageService.storeFile(file, eventById);
                    }
                    fileStorageService.deleteFile(path + "/" + Files.list(path).collect(Collectors.toList()).get(0).getFileName());
                    fileStorageService.storeFile(file, eventById);
                } else if (Files.list(path).collect(Collectors.toList()).isEmpty()) {
                    fileStorageService.storeFile(file, eventById);
                }
            }
        }
//        else {
//            if (Files.exists(path)) {
//                if (!Files.list(path).collect(Collectors.toList()).isEmpty()) {
//                    System.out.println("File: " + path + "/" + Files.list(path).collect(Collectors.toList()).get(0).getFileName());
//                    fileStorageService.deleteFile(path + "/" + Files.list(path).collect(Collectors.toList()).get(0).getFileName());
//                }
//            }
//        }
    }

    public Date findEndDate(Date date, Integer duration) {
        return new Date(date.getTime() + (duration * 60000));
    }

    public Event mapEvent(EventPostDTO newEvent) {
        Event event = new Event();
        event.setBookingName(newEvent.getBookingName());
        event.setBookingEmail(newEvent.getBookingEmail());
        event.setEventStartTime(newEvent.getEventStartTime());
        event.setEventNotes(newEvent.getEventNotes());
        event.setEventDuration(newEvent.getEventDuration());
        event.setEventCategory(eventCategoryRepository.findById(newEvent.getEventCategoryId()).get());
        event.setUser(newEvent.getUser());
        return event;
    }

    public void sendEmail(Event event) {
        ZoneId z = ZoneId.of("Asia/Bangkok");
        String pattern = "E MMM dd, yyyy";
        String patternTime = "HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern(patternTime);

        emailService.sendSimpleMail(event.getBookingEmail(), "[OASIP] " + event.getEventCategory().getEventCategoryName() + " @ " +
                        event.getEventStartTime().atZone(z).format(formatter) + " " + event.getEventStartTime().atZone(z).format(formatterTime) + "-" +
                        event.getEventStartTime().atZone(z).plusMinutes(event.getEventDuration()).format(formatterTime) + " (ICT)",
                "Subject: " + "[OASIP] " + event.getEventCategory().getEventCategoryName() + " @ " +
                        event.getEventStartTime().atZone(z).format(formatter) + " " + event.getEventStartTime().atZone(z).format(formatterTime) + "-" +
                        event.getEventStartTime().atZone(z).plusMinutes(event.getEventDuration()).format(formatterTime) + " (ICT)" + "\n" +
                        "Reply-to: " + event.getBookingEmail() + "\n" +
                        "Booking name: " + event.getBookingName() + "\n" +
                        "Event Category: " + event.getEventCategory().getEventCategoryName() + "\n" +
                        "When: " + event.getEventStartTime().atZone(z).format(formatter) + " " + event.getEventStartTime().atZone(z).format(formatterTime) + "-" +
                        event.getEventStartTime().atZone(z).plusMinutes(event.getEventDuration()).format(formatterTime) + " (ICT)" + "\n" +
                        "Event Note: " + event.getEventNotes(), new Date());
    }

    public Map<String, String> getFile(Path filePath, Event event) throws IOException, HandleExceptionNotFound {
        Map<String, String> fileMap = new HashMap<>();
        if (Files.list(filePath).collect(Collectors.toList()).isEmpty()) {
            fileMap.put("fileName", "");
            fileMap.put("pathFile", filePath.toString());
            return fileMap;
        }
        Path pathFile = Files.list(filePath).collect(Collectors.toList()).get(0);
        System.out.println("PathFile: " + pathFile);
        String fileName = fileStorageService.loadFileAsResource(pathFile.toString(), event).getFilename();
        Resource resource = fileStorageService.loadFileAsResource(fileName, event);
        fileMap.put("fileName", fileName);
//        fileMap.put("pathFile", resource.getURI().toString());
        return fileMap;
    }

    public HandleValidationError addOverlap(EventPostDTO newEvent, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        Date newEventStartTime = Date.from(newEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(newEvent.getEventStartTime()), newEvent.getEventDuration());
        List<EventBlindDTO> eventList = getEventByCurrentTime(newEvent.getEventStartTime(), newEvent.getEventCategoryId());

        for (EventBlindDTO eventBlindDTO : eventList) {
            Date eventStartTime = Date.from(eventBlindDTO.getEventStartTime());
            Date eventEndTime = findEndDate(Date.from(eventBlindDTO.getEventStartTime()), eventBlindDTO.getEventCategory().getEventDuration());
            if (newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventStartTime) ||
                    newEventStartTime.equals(eventStartTime) || newEventStartTime.equals(eventEndTime) ||
                    newEventEndTime.equals(eventStartTime)) {
                errorMap.put("eventStartTime", "StartTime is Overlap");
            }
        }

        if (errorMap.size() <= 0) return null;
        return new HandleValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Bad Requests", "Validation", request.getRequest().getRequestURI(), errorMap);
    }

    public HandleValidationError updateOverlap(EventEditDTO updateEvent, Integer eventId, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        Date newEventStartTime = Date.from(updateEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(updateEvent.getEventStartTime()), updateEvent.getEventDuration());
        List<EventBlindDTO> eventList = getAllEventBlind();
//        List<EventBlindDTO> eventList = getEventByCurrentTime(updateEvent.getEventStartTime(), updateEvent.getEventCategory().getId());

        for (EventBlindDTO eventDTO : eventList) {
            if (updateEvent.getEventCategory().getId().equals(eventDTO.getEventCategory().getId()) && !eventDTO.getId().equals(eventId)) {
                Date eventStartTime = Date.from(eventDTO.getEventStartTime());
                Date eventEndTime = findEndDate(Date.from(eventDTO.getEventStartTime()), eventDTO.getEventCategory().getEventDuration());
                if (newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventStartTime) ||
                        newEventStartTime.equals(eventStartTime) || newEventStartTime.equals(eventEndTime) ||
                        newEventEndTime.equals(eventStartTime)) {
                    errorMap.put("eventStartTime", "StartTime is Overlap");
                }
            }
        }
        if (errorMap.size() <= 0) return null;

        return new HandleValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Bad Requests", "Validation", request.getRequest().getRequestURI(), errorMap);
    }

    public String getEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getPrincipal().toString();
        return email;
    }

    public String getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role =  authentication.getAuthorities().stream().findFirst().get().getAuthority();
        return role;
    }
}
