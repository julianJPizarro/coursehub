package edu.unimagdalena.coursehub.support;

import edu.unimagdalena.coursehub.student.Student;
import edu.unimagdalena.coursehub.student.StudentRepository;
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
class TransactionRollbackIT {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    @Autowired StudentRepository studentRepository;
    @Autowired TransactionRollbackProbeService rollbackProbeService;

    @Test
    void shouldRollbackManagedEntityChangesWhenRuntimeExceptionEscapesTransaction() {
        Student student = studentRepository.saveAndFlush(
                new Student("Rollback Student", "rollback.service@coursehub.edu", LocalDate.of(2000, 3, 3))
        );

        assertThatThrownBy(() -> rollbackProbeService.deactivateThenFail(student.getId()))
                .isInstanceOf(IllegalStateException.class);

        Student reloaded = studentRepository.findById(student.getId()).orElseThrow();
        assertThat(reloaded.isActive()).isTrue();
    }
}
