package edu.unimagdalena.coursehub.course.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateDepartmentDto {
    @NotBlank String name;
}
