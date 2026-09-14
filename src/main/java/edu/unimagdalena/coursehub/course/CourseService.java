package edu.unimagdalena.coursehub.course;

import edu.unimagdalena.coursehub.course.dto.CourseDto;
import edu.unimagdalena.coursehub.course.dto.CreateCourseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Locale;

@Service
@Validated
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseDto create(@Valid CreateCourseDto dto) {
        String code = normalizeCode(dto.getCode());
        if (courseRepository.existsByCode(code)) throw new CourseCodeAlreadyExistsException(code);
        Department department = requireDepartment(dto.getDepartmentId());
        Course saved = courseRepository.save(new Course(code, dto.getName().trim(), dto.getCredits(), department));
        return courseMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public CourseDto findById(@Positive Long id) { return courseMapper.toDto(requireCourse(id)); }

    @Transactional
    public CourseDto changeCredits(@Positive Long courseId, @Min(1) @Max(6) int credits) {
        Course course = requireCourse(courseId); course.changeCredits(credits); return courseMapper.toDto(course);
    }

    @Transactional
    public CourseDto changeDepartment(@Positive Long courseId, @Positive Long departmentId) {
        Course course = requireCourse(courseId); course.changeDepartment(requireDepartment(departmentId)); return courseMapper.toDto(course);
    }

    @Transactional(readOnly = true)
    public List<CourseDto> listByDepartment(String departmentName) {
        return courseMapper.toDtoList(courseRepository.findByDepartment_NameIgnoreCase(departmentName.trim()));
    }

    private Course requireCourse(Long id) { return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id)); }
    private Department requireDepartment(Long id) { return departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id)); }
    private static String normalizeCode(String code) { return code.trim().toUpperCase(Locale.ROOT); }
}
