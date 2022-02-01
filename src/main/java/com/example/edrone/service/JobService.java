package com.example.edrone.service;

import com.example.edrone.configuration.SpringAsyncConfig;
import com.example.edrone.dto.request.CreateJobRequest;
import com.example.edrone.exception.NumberOfStringsExceededException;
import com.example.edrone.model.Job;
import com.example.edrone.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final Random random = new Random();

    public int getRunningJobsCount() {
        return Thread.getAllStackTraces().keySet().stream()
                .filter(e -> e.getState().equals(Thread.State.RUNNABLE))
                .filter(e -> e.getName().contains(SpringAsyncConfig.THREAD_PREFIX))
                .toList()
                .size();
    }

    public Job createJob(CreateJobRequest createJobRequest) {
        validateRequest(createJobRequest);
        Set<String> generatedStrings = generateStringsList(createJobRequest);
        File file = generateFileWithStringsList(generatedStrings);
        Job job = new Job(file);
        return jobRepository.save(job);
    }

    private File generateFileWithStringsList(Set<String> generatedStrings) {
        File file = null;
        try {
            file = new File(UUID.randomUUID() + ".txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            for (String s : generatedStrings) {
                fileOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


    private Set<String> generateStringsList(CreateJobRequest createJobRequest) {
        Set<String> set = new HashSet<>(createJobRequest.getNumberOfString());
        while (set.size() < createJobRequest.getNumberOfString()) {
            String generatedString = generateSingleString(createJobRequest.getLength(), createJobRequest.getAllowedChars());
            set.add(generatedString);
        }
        return set;
    }

    private String generateSingleString(int length, List<Character> allowedChars) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char newChar = allowedChars.get(random.nextInt(allowedChars.size()));
            stringBuilder.append(newChar);
        }
        return stringBuilder.toString();
    }

    public File setFileToResponse(Long id, HttpServletResponse response) {
        File file = null;
        try {
            file = jobRepository.getFileById(id)
                    .map(Job::getFile)
                    .orElseThrow(() -> new ResourceNotFoundException("resource with id " + id + " not found!"));
            FileInputStream fileInputStream = new FileInputStream(file);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            IOUtils.copy(fileInputStream, response.getOutputStream());
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void validateRequest(CreateJobRequest createJobRequest) {
        int realSize = createJobRequest.getAllowedChars().stream().distinct().toList().size();
        int max = 1;
        for (int i = 0; i < createJobRequest.getLength(); i++) {
            max *= realSize;
        }
        if (max < createJobRequest.getNumberOfString())
            throw new NumberOfStringsExceededException(createJobRequest.getNumberOfString(), max);

    }
}
