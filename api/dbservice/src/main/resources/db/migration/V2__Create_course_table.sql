CREATE TABLE IF NOT EXISTS public.courses (
    id uuid DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    name CHARACTER VARYING(255) NOT NULL,
    description CHARACTER VARYING(255) NOT NULL,
    course_level CHARACTER VARYING(15) NOT NULL,
    number_of_classes BIGINT NOT NULL,
    teacher_id uuid,
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES users(id)
);
