package com.example.edrone.repository;

import com.example.edrone.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> getFileById(long id);

    boolean existsByFile(File file);
}
