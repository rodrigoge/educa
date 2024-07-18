CREATE TABLE course_student (
    course_id uuid NOT NULL,
    student_id uuid NOT NULL,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES users(id),
    PRIMARY KEY (course_id, student_id)
);