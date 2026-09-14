package edu.unimagdalena.coursehub.student;

import edu.unimagdalena.coursehub.enrollment.EnrollmentRepository;
import edu.unimagdalena.coursehub.enrollment.EnrollmentStatus;
import edu.unimagdalena.coursehub.student.dto.RegisterStudentDto;
import edu.unimagdalena.coursehub.student.dto.StudentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    @Mock StudentRepository studentRepository;
    @Mock EnrollmentRepository enrollmentRepository;
    private StudentService studentService;

    @BeforeEach void setUp(){ MockitoAnnotations.openMocks(this); studentService=new StudentService(studentRepository,enrollmentRepository,Mappers.getMapper(StudentMapper.class)); }

    @Test void shouldRegisterStudentAndReturnDto(){
        var dto=RegisterStudentDto.builder().name("Ana López").email("  ANA@CourseHub.edu ").birthDate(LocalDate.of(2002,4,10)).build();
        when(studentRepository.existsByEmail("ana@coursehub.edu")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(i->i.getArgument(0));
        StudentDto result=studentService.register(dto);
        assertThat(result.getEmail()).isEqualTo("ana@coursehub.edu");
        ArgumentCaptor<Student> captor=ArgumentCaptor.forClass(Student.class); verify(studentRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Ana López");
    }

    @Test void shouldRejectDuplicatedEmailBeforeSaving(){
        var dto=RegisterStudentDto.builder().name("Ana").email("ana@coursehub.edu").birthDate(LocalDate.of(2002,4,10)).build();
        when(studentRepository.existsByEmail("ana@coursehub.edu")).thenReturn(true);
        assertThatThrownBy(()->studentService.register(dto)).isInstanceOf(EmailAlreadyExistsException.class);
        verify(studentRepository,never()).save(any());
    }

    @Test void shouldRejectDeactivationWhenStudentHasActiveEnrollments(){
        Student student=new Student("Mateo","mateo@coursehub.edu",LocalDate.of(2001,1,1));
        when(studentRepository.findById(10L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.existsByStudent_IdAndStatus(10L,EnrollmentStatus.ACTIVE)).thenReturn(true);
        assertThatThrownBy(()->studentService.deactivate(10L)).isInstanceOf(StudentHasActiveEnrollmentsException.class);
        assertThat(student.isActive()).isTrue();
    }
}
