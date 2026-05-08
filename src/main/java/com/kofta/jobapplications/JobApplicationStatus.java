package com.kofta.jobapplications;

public enum JobApplicationStatus {
    SENT {
        // -> IN_REVIEW | REJECTED
        @Override
        boolean canTransitionTo(JobApplicationStatus nextState) {
            return nextState == IN_REVIEW || nextState == REJECTED;
        }
    },
    IN_REVIEW {
        // -> ACCEPTED | REJECTED
        @Override
        boolean canTransitionTo(JobApplicationStatus nextState) {
            return nextState == ACCEPTED || nextState == REJECTED;
        }
    },
    ACCEPTED {
        // -> None
        @Override
        boolean canTransitionTo(JobApplicationStatus nextState) {
            return false;
        }
    },
    REJECTED {
        // -> None
        @Override
        boolean canTransitionTo(JobApplicationStatus nextState) {
            return false;
        }
    };

    abstract boolean canTransitionTo(JobApplicationStatus nextState);
}
