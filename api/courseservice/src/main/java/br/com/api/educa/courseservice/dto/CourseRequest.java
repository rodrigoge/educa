package br.com.api.educa.courseservice.dto;

import br.com.api.educa.courseservice.db.CourseLevelEnum;
import br.com.api.educa.courseservice.db.Users;

import java.util.List;

public record CourseRequest(
        String name,
        String description,
        CourseLevelEnum courseLevel,
        Integer numberOfClasses,
        Users teacher,
        List<Users> students
) {
}
