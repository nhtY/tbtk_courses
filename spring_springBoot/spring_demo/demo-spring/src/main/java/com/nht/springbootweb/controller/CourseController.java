package com.nht.springbootweb.controller;

import com.nht.springbootweb.domain.Course;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class CourseController {

    @RequestMapping("courses")
    public List<Course> getCourses(){
        return Arrays.asList(
                new Course(1L, "Intro to Automata", "Alan Turing"),
                new Course(2L, "Intro to OOP", "Uncle Bob")
        );
    }
}
