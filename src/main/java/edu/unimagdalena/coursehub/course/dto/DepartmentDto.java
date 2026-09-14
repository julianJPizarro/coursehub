package edu.unimagdalena.coursehub.course.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DepartmentDto {
    Long id;
    String name;
}
