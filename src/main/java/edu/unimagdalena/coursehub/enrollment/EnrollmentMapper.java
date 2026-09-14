package edu.unimagdalena.coursehub.enrollment;

import edu.unimagdalena.coursehub.enrollment.dto.EnrollmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EnrollmentMapper {
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", source = "student.name")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseCode", source = "course.code")
    @Mapping(target = "courseName", source = "course.name")
    EnrollmentDto toDto(Enrollment enrollment);

    List<EnrollmentDto> toDtoList(List<Enrollment> enrollments);
}
