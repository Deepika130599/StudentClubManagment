package org.studentclubmanagement.exceptions;

public class UnauthorizedActionException extends Throwable {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
