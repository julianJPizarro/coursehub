package edu.unimagdalena.coursehub.course.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCourseDto {
    @NotBlank String code;
    @NotBlank String name;
    @Min(1) @Max(6) int credits;
    @NotNull @Positive Long departmentId;
}
