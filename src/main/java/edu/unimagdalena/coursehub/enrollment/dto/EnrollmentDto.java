package edu.unimagdalena.coursehub.enrollment.dto;

import edu.unimagdalena.coursehub.enrollment.EnrollmentStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class EnrollmentDto {
    Long id;
    Long studentId;
    String studentName;
    Long courseId;
    String courseCode;
    String courseName;
    Instant enrolledAt;
    EnrollmentStatus status;
    BigDecimal finalGrade;
}
