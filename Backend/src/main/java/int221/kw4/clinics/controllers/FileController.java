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

//    @PostMapping("/uploadFile")
//    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, Integer id) {
//        String fileName = fileStorageService.storeFile(file, id);
//
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();
//
//        return new UploadFileResponse(fileName, fileDownloadUri,
//                file.getContentType(), file.getSize());
//    }

//    @PostMapping("/uploadMultipleFiles")
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
//    }

//    @GetMapping("/downloadFile/{id}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable  String fileName, HttpServletRequest request) {
//        // Load file as Resource
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
////        System.out.println(resource.getFile().getAbsolutePath());
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            String content = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//            contentType = request.getServletContext().getMimeType(content);
//            System.out.println(resource.getFile().getAbsolutePath());
//            System.out.println("contentType: " + contentType);
//        } catch (IOException ex) {
//            logger.info("Could not determine file type.");
//        }
//
//        // Fallback to the default content type if type could not be determined
//        if(contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"  " + resource.getFilename() + "\"")
//                .body(resource);
//    }

//    @DeleteMapping("/deleteFile/{fileName:.+}")
//    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
//        // Load file as Resource
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//        try {
//            Files.delete(resource.getFile().toPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok().body("File deleted");
//    }

}