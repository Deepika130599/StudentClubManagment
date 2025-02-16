package org.studentclubmanagement.exceptions;

public class ClubLimitExceededException extends Throwable {
    public ClubLimitExceededException(String message) {
        super(message);
    }
}
