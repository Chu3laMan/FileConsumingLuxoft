package co.luxoft.fileconsuming.repository;

import co.luxoft.fileconsuming.entities.CSVFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CSVRepository extends JpaRepository<CSVFile, String> {
}
