package com.example.edrone.rest;


import com.example.edrone.dto.request.CreateJobRequest;
import com.example.edrone.model.Job;
import com.example.edrone.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/jobs")
public class JobResource {

    private final JobService jobService;

    @GetMapping("/count")
    public ResponseEntity<?> getRunningJobsCount() {
        int jobs = jobService.getRunningJobsCount();
        log.info("returning number of running jobs: {}", jobs);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFileById(@PathVariable @NotBlank Long id, HttpServletResponse response) {
        log.info("returning file with id {}", id);
        jobService.setFileToResponse(id, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Validated
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> createNewJob(@Valid @RequestBody CreateJobRequest createJobRequest, HttpServletRequest request) {
        log.info("creating new job with {} strings", createJobRequest.getNumberOfString());
        Job job = jobService.createJob(createJobRequest);
        URI location = ServletUriComponentsBuilder
                .fromContextPath(request)
                .path("jobs/{id}")
                .buildAndExpand(job.getId())
                .toUri();
        return CompletableFuture.completedFuture(location.toString());
    }

}
