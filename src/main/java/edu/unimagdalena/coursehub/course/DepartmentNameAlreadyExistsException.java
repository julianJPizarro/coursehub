package edu.unimagdalena.coursehub.course;

public class DepartmentNameAlreadyExistsException extends RuntimeException {
    public DepartmentNameAlreadyExistsException(String name) { super("A department already exists with name: " + name); }
}
