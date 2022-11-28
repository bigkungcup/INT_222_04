package int221.kw4.clinics.services;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import int221.kw4.clinics.advices.FileStorageException;
import int221.kw4.clinics.advices.HandleExceptionNotFound;
import int221.kw4.clinics.advices.MyFileNotFoundException;
import int221.kw4.clinics.dtos.events.EventPostDTO;
import int221.kw4.clinics.entities.Event;
import int221.kw4.clinics.properties.FileStorageProperties;
import int221.kw4.clinics.repositories.EventRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, Event event) {
        String userDir = event.getUser() != null ? "User/" + "User_" + event.getUser().getId() : "Guest";
        String eventDir = "Event_" + event.getId().toString();
        if (file == null || file.isEmpty()) {
            try {
                Path fileDir = this.fileStorageLocation.resolve(userDir).resolve(eventDir);
                Files.createDirectories(fileDir);
                return null;
            }catch (Exception ex){
                throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }
        }

        if(file != null && !file.isEmpty()){
            String fileName = StringUtils.cleanPath((file.getOriginalFilename()));
            System.out.println("fileName: " + fileName);
            System.out.println("userDir: " + userDir);
            System.out.println("eventDir: " + eventDir);

            try {
                if (fileName.contains("..") || fileName.contains(" ")) {
                    Path fileDir = this.fileStorageLocation.resolve(userDir).resolve(eventDir);
                    Files.createDirectories(fileDir);
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                Path fileDir = this.fileStorageLocation.resolve(userDir).resolve(eventDir);
                System.out.println("fileDir: " + fileDir);
                Path targetLocation = Files.createDirectories(fileDir).resolve(fileName);
                System.out.println("targetLocation: " + targetLocation);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
        return null;
    }

    public Resource loadFileAsResource(String fileName, Event event) throws HandleExceptionNotFound {
        String userDir = event.getUser() != null ? "User/" + "User_" + event.getUser().getId() : "Guest";
        String eventDir = "Event_" + event.getId().toString();
        try {
            Path filePath = this.fileStorageLocation.resolve(userDir).resolve(eventDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            System.out.println(filePath.toUri());
            System.out.println(resource);
            if(resource.exists()) {
                return resource;
            } else {
                throw new HandleExceptionNotFound("File not found " + "eventNo" + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found " + "eventNo" + fileName, ex);
        }
    }

    public Resource loadFileAsResource(String fileName, Integer id) throws HandleExceptionNotFound {
        Event event = eventRepository.findById(id).orElseThrow(() -> new MyFileNotFoundException("Event not found with id " + id));
        String userDir = event.getUser() != null ? "User/" + "User_" + event.getUser().getId() : "Guest";
        String eventDir = "Event_" + event.getId().toString();
        try {
            Path filePath = this.fileStorageLocation.resolve(userDir).resolve(eventDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            System.out.println(filePath.toUri());
            System.out.println(resource);
            if(resource.exists()) {
                return resource;
            } else {
                throw new HandleExceptionNotFound("File not found " + "eventNo" + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found " + "eventNo" + fileName, ex);
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.delete(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("File not found " + fileName, ex);
        }
    }

    public void deleteDir(String dir) {
        try {
            FileUtils.deleteDirectory(new File(dir));
        } catch (IOException ex) {
            throw new FileStorageException("Dir not found " + dir, ex);
        }
    }
}
