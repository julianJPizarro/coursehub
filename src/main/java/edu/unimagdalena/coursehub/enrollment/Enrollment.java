package edu.unimagdalena.coursehub.enrollment;

import edu.unimagdalena.coursehub.course.Course;
import edu.unimagdalena.coursehub.student.Student;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "enrollment", uniqueConstraints = @UniqueConstraint(
        name = "uk_enrollment_student_course", columnNames = {"student_id", "course_id"}))
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_at", nullable = false)
    private Instant enrolledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(name = "final_grade", precision = 3, scale = 2)
    private BigDecimal finalGrade;

    private Enrollment(Student student, Course course, Instant enrolledAt) {
        this.student = student; this.course = course; this.enrolledAt = enrolledAt; this.status = EnrollmentStatus.ACTIVE;
    }

    public static Enrollment enroll(Student student, Course course) { return new Enrollment(student, course, Instant.now()); }
    public void assignFinalGrade(BigDecimal grade) { requireActive("assign a final grade"); requireValidGrade(grade); this.finalGrade = grade; }
    public void complete() {
        requireActive("complete the enrollment");
        if (finalGrade == null) throw new InvalidEnrollmentStateException("A final grade is required before completing an enrollment");
        this.status = EnrollmentStatus.COMPLETED;
    }
    public void cancel() { requireActive("cancel the enrollment"); this.finalGrade = null; this.status = EnrollmentStatus.CANCELLED; }
    private void requireActive(String operation) {
        if (status != EnrollmentStatus.ACTIVE) throw new InvalidEnrollmentStateException("Cannot %s when enrollment status is %s".formatted(operation, status));
    }
    private static void requireValidGrade(BigDecimal grade) {
        if (grade == null || grade.compareTo(BigDecimal.ZERO) < 0 || grade.compareTo(new BigDecimal("5.00")) > 0) throw new InvalidGradeException(grade);
    }
}
