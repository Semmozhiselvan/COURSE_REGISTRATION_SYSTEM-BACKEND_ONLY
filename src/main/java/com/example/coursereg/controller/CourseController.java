
package com.example.coursereg.controller;

import com.example.coursereg.model.*;
import com.example.coursereg.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private EnrollmentRepository enrollRepo;

    @Autowired
    private com.example.coursereg.repository.UserRepository userRepo;

    // Add course (ADMIN)
    @PostMapping("/add")
    public Map<String,Object> addCourse(@RequestBody Course course) {
        courseRepo.save(course);
        return Map.of("msg","Course added");
    }

    // View all courses (authenticated)
    @GetMapping("/all")
    public List<Course> allCourses() {
        return courseRepo.findAll();
    }

    // Enroll (STUDENT)
    @PostMapping("/enroll")
    public Map<String,Object> enroll(@RequestParam Long courseId,
                                      @AuthenticationPrincipal UserDetails ud) {
        var user = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        if (courseRepo.findById(courseId).isEmpty()) return Map.of("error","Course not found");
        Enrollment e = Enrollment.builder().courseId(courseId).userId(user.getId()).build();
        enrollRepo.save(e);
        return Map.of("msg","Enrolled");
    }

    // View enrolled courses (STUDENT)
    @GetMapping("/enrolled")
    public List<Course> enrolled(@AuthenticationPrincipal UserDetails ud) {
        var user = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        List<Enrollment> list = enrollRepo.findByUserId(user.getId());
        return list.stream()
                .map(en -> courseRepo.findById(en.getCourseId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Admin: view enrolled students for a specific course
    @GetMapping("/enrolled/students")
    public List<Map<String,Object>> studentsForCourse(@RequestParam Long courseId) {
        List<Enrollment> list = enrollRepo.findByCourseId(courseId);
        return list.stream().map(en -> {
            var u = userRepo.findById(en.getUserId()).orElse(null);
            return u == null ? Map.of() : Map.of("id", u.getId(), "email", u.getEmail());
        }).collect(Collectors.toList());
    }
}
