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

    private static final String LOG_FILE_PATH = "./logs/";
    private static final String LOG_FILE_NAME = "logfile.log";

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadLogFile() {
        File logDirectory = new File(LOG_FILE_PATH);
        if (!logDirectory.exists()) {
            if (!logDirectory.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        File logFile = new File(logDirectory, LOG_FILE_NAME);
        if (!logFile.exists()) {
            // If the log file doesn't exist, you may want to create it here.
            // You can create an empty file using logFile.createNewFile()
            // or generate log entries and write them to the file.
            // For simplicity, I'll return a 404 status here.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(LOG_FILE_NAME, LOG_FILE_NAME);

        Resource resource = new FileSystemResource(logFile);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
