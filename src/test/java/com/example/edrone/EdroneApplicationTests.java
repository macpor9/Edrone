package com.example.edrone;

import com.example.edrone.dto.request.CreateJobRequest;
import com.example.edrone.exception.NumberOfStringsExceededException;
import com.example.edrone.service.JobService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class EdroneApplicationTests {

    @InjectMocks
    private JobService jobService;


    @Test
    void testValidateRequest_providedValidRequest_noExceptionThrown() {
        CreateJobRequest createJobRequest = new CreateJobRequest(3125, 5, List.of('1', '2', '3', '4', '5'));
        Assertions
                .assertThatNoException()
                .isThrownBy(() -> jobService.validateRequest(createJobRequest));
    }

    @Test
    void testValidateRequest_providedInvalidRequest_throwNumberOfStringsExceededException() {
        CreateJobRequest createJobRequest = new CreateJobRequest(3126, 5, List.of('1', '2', '3', '4', '5'));
        Assertions
                .assertThatThrownBy(() -> jobService.validateRequest(createJobRequest))
                .isInstanceOf(NumberOfStringsExceededException.class);
    }

    @Test
    void testGenerateSingleString() {
        String generatedString = jobService.generateSingleString(5, List.of('a', 'b', 'c'));
        Assertions
                .assertThat(generatedString)
                .hasSize(5)
                .matches(".*[abc].*");
    }

    @Test
    void testGenerateStringSet() {
        CreateJobRequest createJobRequest = new CreateJobRequest(3125, 5, List.of('1', '2', '3', '4', '5'));
        Set<String> generatedStringSet = jobService.generateStringsList(createJobRequest);
        Assertions
                .assertThat(generatedStringSet)
                .hasSize(createJobRequest.getNumberOfString())
                .allMatch(e -> e.length() == createJobRequest.getLength())
                .allMatch(e -> e.matches(".*[12345].*"));

    }


}
