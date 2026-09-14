package edu.unimagdalena.coursehub.course;

import edu.unimagdalena.coursehub.enrollment.Enrollment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "course")
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 20)
    private String code;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false)
    private int credits;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();

    public Course(String code, String name, int credits, Department department) {
        requireValidCredits(credits);
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.department = department;
    }

    public void changeCredits(int credits) { requireValidCredits(credits); this.credits = credits; }
    public void changeDepartment(Department department) {
        if (department == null) throw new IllegalArgumentException("Department is required");
        this.department = department;
    }
    private static void requireValidCredits(int credits) {
        if (credits < 1 || credits > 6) throw new IllegalArgumentException("Credits must be between 1 and 6");
    }
    public List<Enrollment> getEnrollments() { return List.copyOf(enrollments); }
}
