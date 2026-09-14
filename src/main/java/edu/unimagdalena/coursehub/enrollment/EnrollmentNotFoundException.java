package edu.unimagdalena.coursehub.enrollment;

public class EnrollmentNotFoundException extends RuntimeException {
    public EnrollmentNotFoundException(Long id) { super("Enrollment not found: " + id); }
}
