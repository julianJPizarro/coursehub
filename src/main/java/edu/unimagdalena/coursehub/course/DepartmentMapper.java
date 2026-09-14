package edu.unimagdalena.coursehub.course;

import edu.unimagdalena.coursehub.course.dto.DepartmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DepartmentMapper {
    DepartmentDto toDto(Department department);
    List<DepartmentDto> toDtoList(List<Department> departments);
}
