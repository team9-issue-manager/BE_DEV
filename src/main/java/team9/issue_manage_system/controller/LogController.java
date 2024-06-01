package team9.issue_manage_system.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/logs")
public class LogController {

    private static final String LOG_FILE_PATH = "C/Users/rogan/logdir";
    private static final String LOG_FILE_NAME = "logfile.log";

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadLogFile() {
        File logFile = new File(LOG_FILE_PATH, LOG_FILE_NAME);
        if (!logFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(LOG_FILE_NAME, LOG_FILE_NAME);

        Resource resource = new FileSystemResource(logFile);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
