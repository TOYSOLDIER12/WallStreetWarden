package ma.xproce.getrich.web;

import ma.xproce.getrich.service.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {

    @Autowired
    private FileManager fileManager;

    private final String directory = "python/csv/";

    @GetMapping("/download-csv")
    public ResponseEntity<Resource> downloadCsv(@RequestParam("fileName") String fileName) {
        try {
            Path path = Paths.get(directory + fileName + ".csv");
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists()) {
                // File exists, return it immediately
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".csv\"")
                        .contentType(MediaType.parseMediaType("text/csv"))
                        .body(resource);
            } else {
                // If file doesn't exist, download it
                boolean isDownloaded = fileManager.downloadCsv(fileName);

                if (isDownloaded) {
                    // Wait for file to be created
                    int retries = 0;
                    while (!Files.exists(path) && retries < 20) {  // Increased retries and wait time
                        Thread.sleep(1000);  // Wait 1 second between retries
                        retries++;
                    }

                    // If file exists after waiting, return it
                    if (Files.exists(path)) {
                        resource = new UrlResource(path.toUri());
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".csv\"")
                                .contentType(MediaType.parseMediaType("text/csv"))
                                .body(resource);
                    }
                }

                // If download or file creation failed
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
