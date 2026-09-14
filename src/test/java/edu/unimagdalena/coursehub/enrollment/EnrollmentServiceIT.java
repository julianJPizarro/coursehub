package edu.unimagdalena.coursehub.enrollment;

import edu.unimagdalena.coursehub.course.Course;
import edu.unimagdalena.coursehub.course.CourseRepository;
import edu.unimagdalena.coursehub.course.Department;
import edu.unimagdalena.coursehub.course.DepartmentRepository;
import edu.unimagdalena.coursehub.student.Student;
import edu.unimagdalena.coursehub.student.StudentRepository;
import edu.unimagdalena.coursehub.enrollment.dto.EnrollStudentDto;
import edu.unimagdalena.coursehub.enrollment.dto.EnrollmentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class EnrollmentServiceIT {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired EnrollmentService enrollmentService;

    @Test
    void shouldPersistCompleteEnrollmentLifecycle() {
        Department department = departmentRepository.saveAndFlush(new Department("Lifecycle Department"));
        Course course = courseRepository.saveAndFlush(new Course("LIFE-01", "Lifecycle", 3, department));
        Student student = studentRepository.saveAndFlush(new Student("Lifecycle Student", "lifecycle@coursehub.edu", LocalDate.of(2000, 1, 1)));

        EnrollmentDto enrollment = enrollmentService.enroll(EnrollStudentDto.builder().studentId(student.getId()).courseId(course.getId()).build());
        enrollmentService.assignFinalGrade(enrollment.getId(), new BigDecimal("4.75"));
        enrollmentService.complete(enrollment.getId());

        Enrollment reloaded = enrollmentRepository.findById(enrollment.getId()).orElseThrow();
        assertThat(reloaded.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
        assertThat(reloaded.getFinalGrade()).isEqualByComparingTo("4.75");
    }

    @Test
    void shouldRejectDuplicateEnrollmentAtBusinessLayer() {
        Department department = departmentRepository.saveAndFlush(new Department("Duplicate Service Department"));
        Course course = courseRepository.saveAndFlush(new Course("DUP-SRV", "Duplicate Service", 4, department));
        Student student = studentRepository.saveAndFlush(new Student("Duplicate Student", "duplicate.service@coursehub.edu", LocalDate.of(2001, 4, 4)));
        enrollmentService.enroll(EnrollStudentDto.builder().studentId(student.getId()).courseId(course.getId()).build());

        assertThatThrownBy(() -> enrollmentService.enroll(EnrollStudentDto.builder().studentId(student.getId()).courseId(course.getId()).build()))
                .isInstanceOf(DuplicateEnrollmentException.class);
    }
}
