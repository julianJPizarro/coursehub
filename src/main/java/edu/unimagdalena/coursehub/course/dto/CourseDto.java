package edu.unimagdalena.coursehub.course.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CourseDto {
    Long id;
    String code;
    String name;
    int credits;
    Long departmentId;
    String departmentName;
}
