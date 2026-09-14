package edu.unimagdalena.coursehub.enrollment;

public class DuplicateEnrollmentException extends RuntimeException {
    public DuplicateEnrollmentException(Long studentId, Long courseId) { super("Student %d is already enrolled in course %d".formatted(studentId, courseId)); }
}
