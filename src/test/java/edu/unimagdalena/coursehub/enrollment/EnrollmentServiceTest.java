package edu.unimagdalena.coursehub.enrollment;

import edu.unimagdalena.coursehub.course.Course;
import edu.unimagdalena.coursehub.course.CourseRepository;
import edu.unimagdalena.coursehub.course.Department;
import edu.unimagdalena.coursehub.enrollment.dto.EnrollStudentDto;
import edu.unimagdalena.coursehub.enrollment.dto.EnrollmentDto;
import edu.unimagdalena.coursehub.student.InactiveStudentException;
import edu.unimagdalena.coursehub.student.Student;
import edu.unimagdalena.coursehub.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test; import org.mapstruct.factory.Mappers; import org.mockito.Mock; import org.mockito.MockitoAnnotations; import java.math.BigDecimal; import java.time.LocalDate; import java.util.Optional;
import static org.assertj.core.api.Assertions.*; import static org.mockito.ArgumentMatchers.any; import static org.mockito.Mockito.*;
class EnrollmentServiceTest {
 @Mock EnrollmentRepository enrollmentRepository; @Mock
    StudentRepository studentRepository; @Mock
    CourseRepository courseRepository; private EnrollmentService enrollmentService;
 @BeforeEach void setUp(){MockitoAnnotations.openMocks(this);enrollmentService=new EnrollmentService(enrollmentRepository,studentRepository,courseRepository,Mappers.getMapper(EnrollmentMapper.class));}
 @Test void shouldEnrollActiveStudentAndReturnFlattenedDto(){
  Student student=activeStudent(); Course course=course(); when(studentRepository.findById(1L)).thenReturn(Optional.of(student)); when(courseRepository.findById(2L)).thenReturn(Optional.of(course)); when(enrollmentRepository.existsByStudent_IdAndCourse_Id(1L,2L)).thenReturn(false); when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(i->i.getArgument(0));
  EnrollmentDto result=enrollmentService.enroll(EnrollStudentDto.builder().studentId(1L).courseId(2L).build()); assertThat(result.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE); assertThat(result.getStudentName()).isEqualTo("Ana"); assertThat(result.getCourseCode()).isEqualTo("JAVA-21");
 }
 @Test void shouldStopWhenStudentInactive(){ Student s=activeStudent();s.deactivate();when(studentRepository.findById(1L)).thenReturn(Optional.of(s));assertThatThrownBy(()->enrollmentService.enroll(EnrollStudentDto.builder().studentId(1L).courseId(2L).build())).isInstanceOf(InactiveStudentException.class);verifyNoInteractions(courseRepository); }
 @Test void shouldRequireGradeBeforeCompletion(){ Enrollment e=Enrollment.enroll(activeStudent(),course());when(enrollmentRepository.findById(8L)).thenReturn(Optional.of(e));assertThatThrownBy(()->enrollmentService.complete(8L)).isInstanceOf(InvalidEnrollmentStateException.class); }
 @Test void shouldAssignGradeAndComplete(){ Enrollment e=Enrollment.enroll(activeStudent(),course());when(enrollmentRepository.findById(8L)).thenReturn(Optional.of(e));enrollmentService.assignFinalGrade(8L,new BigDecimal("4.50"));EnrollmentDto result=enrollmentService.complete(8L);assertThat(result.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);assertThat(result.getFinalGrade()).isEqualByComparingTo("4.50"); }
 private static Student activeStudent(){return new Student("Ana","ana@coursehub.edu",LocalDate.of(2002,1,1));} private static Course course(){return new Course("JAVA-21","Modern Java",4,new Department("Engineering"));}
}
