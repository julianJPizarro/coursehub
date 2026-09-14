package edu.unimagdalena.coursehub.student;

import edu.unimagdalena.coursehub.course.Course;
import edu.unimagdalena.coursehub.course.CourseRepository;
import edu.unimagdalena.coursehub.course.Department;
import edu.unimagdalena.coursehub.course.DepartmentRepository;
import edu.unimagdalena.coursehub.enrollment.EnrollmentService;
import edu.unimagdalena.coursehub.enrollment.dto.EnrollStudentDto;
import edu.unimagdalena.coursehub.student.dto.RegisterStudentDto;
import edu.unimagdalena.coursehub.student.dto.StudentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class StudentServiceIT {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    @Autowired StudentService studentService;
    @Autowired StudentRepository studentRepository;
    @Autowired DepartmentRepository departmentRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired EnrollmentService enrollmentService;

    @Test
    void shouldRegisterAndPersistNormalizedEmail() {
        StudentDto student = studentService.register(RegisterStudentDto.builder().name("Service Student").email(" SERVICE.IT@CourseHub.edu ").birthDate(LocalDate.of(2001, 5, 5)).build());

        Student reloaded = studentRepository.findById(student.getId()).orElseThrow();
        assertThat(reloaded.getEmail()).isEqualTo("service.it@coursehub.edu");
    }

    @Test
    void shouldPreventDeactivationWhenActiveEnrollmentExists() {
        StudentDto student = studentService.register(RegisterStudentDto.builder().name("Active Enrollment Student").email("active.enrollment@coursehub.edu").birthDate(LocalDate.of(2001, 6, 6)).build());
        Department department = departmentRepository.saveAndFlush(new Department("Service IT Engineering"));
        Course course = courseRepository.saveAndFlush(new Course("SERV-IT-01", "Services", 4, department));
        enrollmentService.enroll(EnrollStudentDto.builder().studentId(student.getId()).courseId(course.getId()).build());

        assertThatThrownBy(() -> studentService.deactivate(student.getId()))
                .isInstanceOf(StudentHasActiveEnrollmentsException.class);

        assertThat(studentRepository.findById(student.getId()).orElseThrow().isActive()).isTrue();
    }
}
