package br.com.api.educa.courseservice.api;

import br.com.api.educa.courseservice.dto.CourseRequest;
import br.com.api.educa.courseservice.dto.CourseResponse;
import br.com.api.educa.courseservice.services.CourseService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@Log4j2
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        log.info("CourseController.createUser - starting flow");
        var response = courseService.createCourse(courseRequest);
        log.info("CourseController.createCourse - finishing flow");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
