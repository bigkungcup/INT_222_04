package int221.kw4.clinics.controllers;


import int221.kw4.clinics.dtos.files.UploadFileResponse;
import int221.kw4.clinics.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/files")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{eventId}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable  String fileName,@PathVariable  Integer eventId, HttpServletRequest request) throws IOException {

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName, eventId);
        System.out.println(resource.getFile().getAbsolutePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"  " + resource.getFilename() + "\"")
                .body(resource);
    }
}