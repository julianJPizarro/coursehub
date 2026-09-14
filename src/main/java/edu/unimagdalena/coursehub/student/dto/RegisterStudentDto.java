package edu.unimagdalena.coursehub.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class RegisterStudentDto {
    @NotBlank String name;
    @NotBlank @Email String email;
    @Past LocalDate birthDate;
}
