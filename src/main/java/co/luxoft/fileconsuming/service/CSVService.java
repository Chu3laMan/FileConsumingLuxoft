package co.luxoft.fileconsuming.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


import co.luxoft.fileconsuming.entities.CSVFile;
import co.luxoft.fileconsuming.helper.CSVHelper;
import co.luxoft.fileconsuming.repository.CSVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CSVService {
  @Autowired
  CSVRepository csvRepository;

  public void save(MultipartFile file) {
    try {
      List<CSVFile> csvFiles = CSVHelper.csvToTutorials(file.getInputStream());
      csvRepository.saveAll(csvFiles);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public ByteArrayInputStream load() {
    List<CSVFile> csvFiles = csvRepository.findAll();

    ByteArrayInputStream in = CSVHelper.tutorialsToCSV(csvFiles);
    return in;
  }

  public void deleteFile(String id) {
    csvRepository.deleteById(id);
  }

  public CSVFile findById(String id) {
    return csvRepository.findById(id).orElse(null);
  }

  public List<CSVFile> getAllTutorials() {
    return csvRepository.findAll();
  }
}
