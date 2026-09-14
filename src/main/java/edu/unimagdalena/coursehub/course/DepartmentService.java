package edu.unimagdalena.coursehub.course;

import edu.unimagdalena.coursehub.course.dto.CreateDepartmentDto;
import edu.unimagdalena.coursehub.course.dto.DepartmentDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public DepartmentDto create(@Valid CreateDepartmentDto dto) {
        String name = dto.getName().trim();
        if (departmentRepository.existsByNameIgnoreCase(name)) throw new DepartmentNameAlreadyExistsException(name);
        return departmentMapper.toDto(departmentRepository.save(new Department(name)));
    }

    @Transactional(readOnly = true)
    public DepartmentDto findById(@Positive Long id) { return departmentMapper.toDto(requireDepartment(id)); }

    @Transactional(readOnly = true)
    public List<DepartmentDto> findAll() { return departmentMapper.toDtoList(departmentRepository.findAll()); }

    private Department requireDepartment(Long id) { return departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id)); }
}
