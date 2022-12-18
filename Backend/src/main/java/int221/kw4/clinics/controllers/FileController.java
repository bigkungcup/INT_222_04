package int221.kw4.clinics.controllers;


import int221.kw4.clinics.advices.HandleExceptionForbidden;
import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.properties.FileStorageProperties;
import int221.kw4.clinics.repositories.EventRepository;
import int221.kw4.clinics.services.EventService;
import int221.kw4.clinics.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/files")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileStorageService fileStorageService;

    private final EventService eventService;

    private final EventRepository eventRepository;

    private final FileStorageProperties fileStorageProperties;

    public FileController(FileStorageService fileStorageService, EventService eventService, EventRepository eventRepository, FileStorageProperties fileStorageProperties) {
        this.fileStorageService = fileStorageService;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.fileStorageProperties = fileStorageProperties;
    }

    @GetMapping("/{eventId}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable  String fileName,@PathVariable  Integer eventId, HttpServletRequest request) throws IOException, HandleExceptionNotFound {

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName, eventId);
        System.out.println(resource.getFile().getAbsolutePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"  " + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity deleteFile(@PathVariable  Integer eventId) throws IOException {
        Event eventById = eventRepository.getById(eventId);
        String userDir = eventById.getBookingEmail() != null ? "User/" + "User_" + eventById.getBookingEmail() : "Guest";
        Path path = Paths.get(fileStorageProperties.getUploadDir() + "/" + userDir + "/" + "Event_" + eventById.getId());
        fileStorageService.deleteFile(path + "/" + Files.list(path).collect(Collectors.toList()).get(0).getFileName());
        return ResponseEntity.ok().build();
    }

}