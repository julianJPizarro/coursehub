package edu.unimagdalena.coursehub.student;

import edu.unimagdalena.coursehub.student.dto.StudentDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface StudentMapper {
    StudentDto toDto(Student student);
    List<StudentDto> toDtoList(List<Student> students);
}
