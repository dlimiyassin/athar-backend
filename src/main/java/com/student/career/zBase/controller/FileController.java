package com.student.career.zBase.controller;

import com.student.career.zBase.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/get-image")
    public ResponseEntity<Resource>
    getImage(@RequestParam String path) {
        try {
            Resource image = fileService.getImageByPath(path);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                    .body(image);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteImage(@RequestParam String path) {
        fileService.deleteImageByPath(path);
        return ResponseEntity.noContent().build();
    }
}
