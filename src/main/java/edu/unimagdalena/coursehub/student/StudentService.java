package edu.unimagdalena.coursehub.student;

import edu.unimagdalena.coursehub.enrollment.EnrollmentRepository;
import edu.unimagdalena.coursehub.enrollment.EnrollmentStatus;
import edu.unimagdalena.coursehub.student.dto.RegisterStudentDto;
import edu.unimagdalena.coursehub.student.dto.StudentDto;
import edu.unimagdalena.coursehub.student.dto.UpdateStudentDto;
import jakarta.validation.Valid;
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
public class StudentService {
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentDto register(@Valid RegisterStudentDto dto) {
        String email = normalizeEmail(dto.getEmail());
        if (studentRepository.existsByEmail(email)) throw new EmailAlreadyExistsException(email);
        Student saved = studentRepository.save(new Student(dto.getName().trim(), email, dto.getBirthDate()));
        return studentMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public StudentDto findById(@Positive Long id) { return studentMapper.toDto(requireStudent(id)); }

    @Transactional(readOnly = true)
    public List<StudentDto> searchActiveStudents(String name) {
        String term = name == null ? "" : name.trim();
        return studentMapper.toDtoList(studentRepository.findByNameContainingIgnoreCaseAndActiveTrue(term));
    }

    @Transactional
    public StudentDto update(@Positive Long id, @Valid UpdateStudentDto dto) {
        Student student = requireStudent(id);
        String email = normalizeEmail(dto.getEmail());
        if (studentRepository.existsByEmailAndIdNot(email, id)) throw new EmailAlreadyExistsException(email);
        student.updatePersonalData(dto.getName().trim(), email, dto.getBirthDate());
        return studentMapper.toDto(student);
    }

    @Transactional
    public StudentDto activate(@Positive Long id) {
        Student student = requireStudent(id); student.activate(); return studentMapper.toDto(student);
    }

    @Transactional
    public StudentDto deactivate(@Positive Long id) {
        Student student = requireStudent(id);
        if (enrollmentRepository.existsByStudent_IdAndStatus(id, EnrollmentStatus.ACTIVE)) throw new StudentHasActiveEnrollmentsException(id);
        student.deactivate();
        return studentMapper.toDto(student);
    }

    private Student requireStudent(Long id) { return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)); }
    private static String normalizeEmail(String email) { return email.trim().toLowerCase(Locale.ROOT); }
}
