package edu.unimagdalena.coursehub.student;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) { super("A student already exists with email: " + email); }
}
