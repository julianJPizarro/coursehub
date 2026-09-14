package edu.unimagdalena.coursehub.enrollment;

import edu.unimagdalena.coursehub.course.Course;
import edu.unimagdalena.coursehub.course.CourseRepository;
import edu.unimagdalena.coursehub.course.Department;
import edu.unimagdalena.coursehub.course.DepartmentRepository;
import edu.unimagdalena.coursehub.student.Student;
import edu.unimagdalena.coursehub.student.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class EnrollmentRepositoryIT {

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

    @Test
    void shouldCalculateAverageGradeUsingJpql() {
        Department department = departmentRepository.saveAndFlush(new Department("Engineering AVG"));
        Course course = courseRepository.saveAndFlush(new Course("JAVA-AVG", "Modern Java", 4, department));
        Student one = studentRepository.saveAndFlush(new Student("Student One", "one.avg@coursehub.edu", LocalDate.of(2000, 1, 1)));
        Student two = studentRepository.saveAndFlush(new Student("Student Two", "two.avg@coursehub.edu", LocalDate.of(2001, 1, 1)));

        Enrollment enrollmentOne = enrollmentRepository.saveAndFlush(Enrollment.enroll(one, course));
        enrollmentOne.assignFinalGrade(new BigDecimal("4.00"));
        enrollmentOne.complete();
        enrollmentRepository.saveAndFlush(enrollmentOne);

        Enrollment enrollmentTwo = enrollmentRepository.saveAndFlush(Enrollment.enroll(two, course));
        enrollmentTwo.assignFinalGrade(new BigDecimal("5.00"));
        enrollmentTwo.complete();
        enrollmentRepository.saveAndFlush(enrollmentTwo);

        Double average = enrollmentRepository.calculateAverageGrade(course.getId());

        assertThat(average).isEqualTo(4.5d);
    }
}
