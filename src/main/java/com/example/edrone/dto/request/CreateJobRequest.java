package com.example.edrone.dto.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CreateJobRequest {
    @NotNull
    private int numberOfString;

    @NotNull
    private int length;

    @Valid
    @Size(min = 1)
    private List<@NotNull Character> allowedChars;
}
