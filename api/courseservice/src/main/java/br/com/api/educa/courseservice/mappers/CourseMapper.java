package br.com.api.educa.courseservice.mappers;

import br.com.api.educa.courseservice.db.Course;
import br.com.api.educa.courseservice.dto.CourseRequest;
import br.com.api.educa.courseservice.dto.CourseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    Course toCourse(CourseRequest courseRequest);

    CourseResponse toCourseResponse(Course course);
}
