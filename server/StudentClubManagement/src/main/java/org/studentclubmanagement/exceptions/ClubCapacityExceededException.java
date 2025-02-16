package org.studentclubmanagement.exceptions;

public class ClubCapacityExceededException extends Throwable {
    public ClubCapacityExceededException(String message) {
        super(message);
    }
}
