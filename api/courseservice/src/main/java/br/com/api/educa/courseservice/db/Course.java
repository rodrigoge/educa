package br.com.api.educa.courseservice.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "course_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevelEnum courseLevel;

    @Column(name = "number_of_classes", nullable = false)
    private Integer numberOfClasses;

    @OneToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Users teacher;

    @ManyToMany
    @JoinTable(
            name = "user_x_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Users> students;
}
