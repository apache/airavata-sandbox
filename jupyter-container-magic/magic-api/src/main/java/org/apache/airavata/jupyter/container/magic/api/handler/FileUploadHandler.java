package org.apache.airavata.jupyter.container.magic.api.handler;

import org.apache.airavata.jupyter.container.magic.api.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadHandler {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                  RedirectAttributes redirectAttributes) {

        try {
            String uploadPath =  storageService.store(file);
            System.out.println("Uploaded to " + uploadPath);
            return new ResponseEntity<String>(uploadPath, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
