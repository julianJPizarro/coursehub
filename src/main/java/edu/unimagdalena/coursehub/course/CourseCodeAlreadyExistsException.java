package edu.unimagdalena.coursehub.course;

public class CourseCodeAlreadyExistsException extends RuntimeException {
    public CourseCodeAlreadyExistsException(String code) { super("A course already exists with code: " + code); }
}
