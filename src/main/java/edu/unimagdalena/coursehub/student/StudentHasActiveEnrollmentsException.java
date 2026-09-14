package edu.unimagdalena.coursehub.student;

public class StudentHasActiveEnrollmentsException extends RuntimeException {
    public StudentHasActiveEnrollmentsException(Long id) { super("Student has active enrollments and cannot be deactivated: " + id); }
}
