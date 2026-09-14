package edu.unimagdalena.coursehub.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EnrollStudentDto {
    @NotNull @Positive Long studentId;
    @NotNull @Positive Long courseId;
}
