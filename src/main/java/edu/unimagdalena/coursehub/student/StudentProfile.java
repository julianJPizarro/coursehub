package edu.unimagdalena.coursehub.student;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "student_profile")
public class StudentProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    @Column(length = 1000)
    private String biography;
    @Column(name = "github_url", length = 250)
    private String githubUrl;
    @Column(name = "linkedin_url", length = 250)
    private String linkedinUrl;

    public StudentProfile(Student student, String biography, String githubUrl, String linkedinUrl) {
        this.student = student;
        this.biography = biography;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
    }
}
