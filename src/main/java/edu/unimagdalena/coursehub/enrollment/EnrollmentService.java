package edu.unimagdalena.coursehub.enrollment;

import edu.unimagdalena.coursehub.course.Course;
import edu.unimagdalena.coursehub.course.CourseNotFoundException;
import edu.unimagdalena.coursehub.course.CourseRepository;
import edu.unimagdalena.coursehub.enrollment.dto.EnrollStudentDto;
import edu.unimagdalena.coursehub.enrollment.dto.EnrollmentDto;
import edu.unimagdalena.coursehub.student.InactiveStudentException;
import edu.unimagdalena.coursehub.student.Student;
import edu.unimagdalena.coursehub.student.StudentNotFoundException;
import edu.unimagdalena.coursehub.student.StudentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Transactional
    public EnrollmentDto enroll(@Valid EnrollStudentDto dto) {
        Long studentId = dto.getStudentId(); Long courseId = dto.getCourseId();
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));
        if (!student.isActive()) throw new InactiveStudentException(studentId);
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException(courseId));
        if (enrollmentRepository.existsByStudent_IdAndCourse_Id(studentId, courseId)) throw new DuplicateEnrollmentException(studentId, courseId);
        return enrollmentMapper.toDto(enrollmentRepository.save(Enrollment.enroll(student, course)));
    }

    @Transactional
    public EnrollmentDto cancel(@Positive Long enrollmentId) {
        Enrollment enrollment = requireEnrollment(enrollmentId); enrollment.cancel(); return enrollmentMapper.toDto(enrollment);
    }

    @Transactional
    public EnrollmentDto assignFinalGrade(@Positive Long enrollmentId,
            @NotNull @DecimalMin("0.00") @DecimalMax("5.00") BigDecimal grade) {
        Enrollment enrollment = requireEnrollment(enrollmentId); enrollment.assignFinalGrade(grade); return enrollmentMapper.toDto(enrollment);
    }

    @Transactional
    public EnrollmentDto complete(@Positive Long enrollmentId) {
        Enrollment enrollment = requireEnrollment(enrollmentId); enrollment.complete(); return enrollmentMapper.toDto(enrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDto> listStudentEnrollments(@Positive Long studentId) {
        if (!studentRepository.existsById(studentId)) throw new StudentNotFoundException(studentId);
        return enrollmentMapper.toDtoList(enrollmentRepository.findByStudent_IdOrderByEnrolledAtDesc(studentId));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDto> listCourseEnrollments(@Positive Long courseId) {
        if (!courseRepository.existsById(courseId)) throw new CourseNotFoundException(courseId);
        return enrollmentMapper.toDtoList(enrollmentRepository.findByCourse_IdOrderByEnrolledAtDesc(courseId));
    }

    private Enrollment requireEnrollment(Long id) { return enrollmentRepository.findById(id).orElseThrow(() -> new EnrollmentNotFoundException(id)); }
}
