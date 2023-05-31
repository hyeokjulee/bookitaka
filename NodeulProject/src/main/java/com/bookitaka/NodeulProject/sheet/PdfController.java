package com.bookitaka.NodeulProject.sheet;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
public class PdfController {

    @GetMapping(value = "/pdfjs/{fileName}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPdf(@PathVariable String fileName) throws IOException {
        Resource resource = new ClassPathResource("/pdfjs/" + fileName );
        return Files.readAllBytes(resource.getFile().toPath());
    }

}
