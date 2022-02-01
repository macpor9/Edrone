package com.example.edrone.exception;

import lombok.Getter;

@Getter
public class NumberOfStringsExceededException extends RuntimeException {

    private final String message;

    public NumberOfStringsExceededException(int numberOfStrings, int maxNumberOfStrings) {
        this.message = "number of strings exceeded for value : " + numberOfStrings + ", max number of strings is: " + maxNumberOfStrings;
    }
}
