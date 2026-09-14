package edu.unimagdalena.coursehub.student;

public class InactiveStudentException extends RuntimeException {
    public InactiveStudentException(Long id) { super("Inactive student cannot enroll: " + id); }
}
