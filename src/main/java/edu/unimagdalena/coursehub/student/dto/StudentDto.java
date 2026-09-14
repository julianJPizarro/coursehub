package edu.unimagdalena.coursehub.student.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class StudentDto {
    Long id;
    String name;
    String email;
    LocalDate birthDate;
    boolean active;
}
