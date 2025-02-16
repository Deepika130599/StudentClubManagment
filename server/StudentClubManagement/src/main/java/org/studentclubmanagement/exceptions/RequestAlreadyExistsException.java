package org.studentclubmanagement.exceptions;

public class RequestAlreadyExistsException extends Throwable {
    public RequestAlreadyExistsException(String message) {
        super(message);
    }
}
