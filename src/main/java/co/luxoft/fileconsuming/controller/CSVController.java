package co.luxoft.fileconsuming.controller;

import java.util.List;


import co.luxoft.fileconsuming.entities.CSVFile;
import co.luxoft.fileconsuming.helper.CSVHelper;
import co.luxoft.fileconsuming.message.ResponseMessage;
import co.luxoft.fileconsuming.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin("http://localhost:9090")
@RestController
@RequestMapping("/api/csv")
public class CSVController {

  @Autowired
  private CSVService csvService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (CSVHelper.hasCSVFormat(file)) {
      try {
        csvService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }

    message = "Please upload a csv file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @GetMapping("/csvfiles")
  public ResponseEntity<List<CSVFile>> getCSVFiles() {
    try {
      List<CSVFile> tutorials = csvService.getAllTutorials();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "simplecsv.csv";
    InputStreamResource file = new InputStreamResource(csvService.load());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }

  @PostMapping("/delete/file/{id}")
  public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String id) {
    String message = "";
    CSVFile csvFile = csvService.findById(id);
    if(csvFile != null) {
      csvService.deleteFile(id);
      message = "The file has been deleted successfully ";
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }
    message = "The file with specified ID does not exist!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));

  }

}
