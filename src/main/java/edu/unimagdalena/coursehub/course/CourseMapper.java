package edu.unimagdalena.coursehub.course;

import edu.unimagdalena.coursehub.course.dto.CourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CourseMapper {
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    CourseDto toDto(Course course);

    List<CourseDto> toDtoList(List<Course> courses);
}
