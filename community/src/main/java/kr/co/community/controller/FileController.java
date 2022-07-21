package kr.co.community.controller;

import kr.co.community.model.File;
import kr.co.community.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> download(@PathVariable("id") Long id) throws IOException {
        return fileService.downloadFileById(id);
    }

    @PostMapping("/summernote")
    public ResponseEntity<?> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        List<File> images = fileService.saveImages(multipartFiles);
        return ResponseEntity.ok().body(images);
    }
}
