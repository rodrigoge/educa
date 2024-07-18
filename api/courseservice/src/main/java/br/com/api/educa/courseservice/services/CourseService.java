package br.com.api.educa.courseservice.services;

import br.com.api.educa.courseservice.db.CourseRepository;
import br.com.api.educa.courseservice.dto.CourseRequest;
import br.com.api.educa.courseservice.dto.CourseResponse;
import br.com.api.educa.courseservice.mappers.CourseMapper;
import br.com.api.educa.courseservice.validators.CourseValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CourseService {

    private final CourseValidator courseValidator;
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;

    public CourseService(CourseValidator courseValidator,
                         CourseMapper courseMapper,
                         CourseRepository courseRepository) {
        this.courseValidator = courseValidator;
        this.courseMapper = courseMapper;
        this.courseRepository = courseRepository;
    }

    public CourseResponse createCourse(CourseRequest courseRequest) {
        log.info("CourseService.createCourse - starting flow");
        validateUserFields(courseRequest);
        var course = courseMapper.toCourse(courseRequest);
        log.info("CourseService.createCourse - saving course in database");
        var courseSaved = courseRepository.save(course);
        log.info("CourseService.createCourse - mapping response to valid object");
        var courseResponse = courseMapper.toCourseResponse(courseSaved);
        log.info("CourseService.createCourse - exiting flow");
        return courseResponse;
    }

    private void validateUserFields(CourseRequest courseRequest) {
        log.info("UserService.validateUserFields - entering flow");
        if (!courseRequest.name().isEmpty()) courseValidator.hasFieldValid(courseRequest.name());
        if (!courseRequest.description().isEmpty()) courseValidator.hasFieldValid(courseRequest.description());
        if (!courseRequest.courseLevel().name().isEmpty())
            courseValidator.hasFieldValid(courseRequest.courseLevel().name());
        if (!courseRequest.numberOfClasses().toString().isEmpty())
            courseValidator.hasFieldValid(courseRequest.numberOfClasses().toString());
        if (!courseRequest.teacher().getName().isEmpty())
            courseValidator.hasFieldValid(courseRequest.teacher().getName());
        log.info("UserService.validateUserFields - finishing flow");
    }
}
