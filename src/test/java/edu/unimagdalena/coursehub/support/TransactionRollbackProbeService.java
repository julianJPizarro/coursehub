package edu.unimagdalena.coursehub.support;
import edu.unimagdalena.coursehub.student.Student; import edu.unimagdalena.coursehub.student.StudentRepository; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor public class TransactionRollbackProbeService {
 private final StudentRepository studentRepository;
 @Transactional public void deactivateThenFail(Long studentId){ Student student=studentRepository.findById(studentId).orElseThrow(); student.deactivate(); throw new IllegalStateException("Intentional failure used to demonstrate rollback"); }
}
