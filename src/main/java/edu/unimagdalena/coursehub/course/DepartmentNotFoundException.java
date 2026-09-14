package edu.unimagdalena.coursehub.course;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) { super("Department not found: " + id); }
}
