package com.kofta.errors;

import com.kofta.jobapplications.JobApplicationStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(
        JobApplicationStatus from,
        JobApplicationStatus to
    ) {
        super(
            "Cannot transition Application Status from " + from + " to " + to
        );
    }
}
