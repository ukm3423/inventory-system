package com.masterservice.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masterservice.dto.CourseDTO;
import com.masterservice.models.Course;
import com.masterservice.services.CourseService;

import jakarta.validation.Valid;

// @CrossOrigin(origins = "http://localhost:8081")
@CrossOrigin
@RestController
@RequestMapping("/course")
public class CourseController {

    /**
     * * ===========================================================================
     * * ======================== Module : CourseController ========================
     * * ======================== Created By : Umesh Kumar =========================
     * * ======================== Created On : 04-06-2024 ==========================
     * * ===========================================================================
     * * | Code Status : On
     */
    @Autowired
    private CourseService courseService;

    /**
     * * Add a New Course
     * * API : http://localhost:8080/api/course/add
     */
    @PostMapping("/add")
    public ResponseEntity<Map<Object, Object>> addCourse(@Validated @RequestBody CourseDTO req,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Construct error response
            Map<Object, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("error", "Validation Error");
            errorResponse.put("message",
                    "Validation failed for object 'categoryRequest'. Error count: " + bindingResult.getErrorCount());

            List<Map<String, String>> errors = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                Map<String, String> error = new HashMap<>();
                error.put("field", fieldError.getField());
                error.put("message", fieldError.getDefaultMessage());
                errors.add(error);
            }
            errorResponse.put("errors", errors);

            return ResponseEntity.badRequest().body(errorResponse);
        }
        Course checkCourseIdExisting = courseService.searchByCourseId(req.getCourseId());
        if (checkCourseIdExisting != null)
            throw new IllegalStateException("Course Id already exists !!");

        Course checkCourseExisting = courseService.searchByCourseName(req.getCourseName());
        if (checkCourseExisting != null)
            throw new IllegalStateException("Course Name already exists !!");

        Map<Object, Object> resp = new HashMap<>();

        Course course = courseService.addCourse(req);

        resp.put("data", course);
        resp.put("message", "Course Added Successfully");
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /**
     * * Update Course BY Id
     * * API : http://localhost:8080/api/course/update/1
     * 
     * @param courseId
     * @param req
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<Object, Object>> updateCourse(@PathVariable("id") Long courseId,
            @Valid @RequestBody CourseDTO req,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Construct error response
            Map<Object, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("error", "Validation Error");
            errorResponse.put("message",
                    "Validation failed for object 'categoryRequest'. Error count: " + bindingResult.getErrorCount());

            List<Map<String, String>> errors = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                Map<String, String> error = new HashMap<>();
                error.put("field", fieldError.getField());
                error.put("message", fieldError.getDefaultMessage());
                errors.add(error);
            }
            errorResponse.put("errors", errors);

            return ResponseEntity.badRequest().body(errorResponse);
        }

        Course checkIdExisting = courseService.searchByCourseId(req.getCourseId());
        if (checkIdExisting != null && !checkIdExisting.getId().equals(courseId)) {
            throw new IllegalStateException("Course Id already exists !");
        }

        Course checkCourseExisting = courseService.searchByCourseName(req.getCourseName());
        if (checkCourseExisting != null && !checkCourseExisting.getId().equals(courseId)) {
            throw new IllegalStateException("Course Name already exists !");
        }
        Course course = courseService.updateCourseById(courseId, req);
        if (course == null)
            throw new IllegalStateException("Course Not Found of Id : " + courseId);

        Map<Object, Object> res = new HashMap<>();
        res.put("message", "Category Updated Successfully");
        res.put("data", course);
        res.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    /**
     * * Delete Course By Id
     * * API : http://localhost:8080/api/course/delete/1
     * 
     * @param id
     * @return
     */
    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<Object, Object>> deleteCourse(@PathVariable Long id) {

        Boolean isdeleted = courseService.deleteCourseById(id);
        Map<Object, Object> resp = new HashMap<Object, Object>();

        if (isdeleted) {
            resp.put("message", "Deleted Successfully");
            resp.put("data", id);
            resp.put("status", true);
        } else {
            resp.put("status", false);
            resp.put("message", "Record Not Found !!");
            resp.put("data", id);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    /**
     * * Get Course By Id
     * * API : http://localhost:8080/api/course/get/1
     * 
     * @param courseId
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Map<Object, Object>> getCategoryById(@PathVariable("id") Long courseId) {

        Course course = courseService.getCourseById(courseId);

        if (course == null)
            throw new IllegalStateException("Course Details Not Found of Id : " + courseId);

        Map<Object, Object> resp = new HashMap<Object, Object>();

        resp.put("message", "Course Details Retrieved Successfully");
        resp.put("data", course);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    /**
     * * Get All Course Details
     * * API : http://localhost:8080/api/course/get-list
     * 
     * @param offset
     * @param limit
     * @return
     */
    @GetMapping("/get-list")
    public ResponseEntity<Map<Object, Object>> getCategoryList(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "5") int limit) {

        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").descending());
        Page<Course> coursePage = courseService.getCoursesList(pageable);

        List<Course> categoryList = coursePage.getContent();

        Map<Object, Object> resp = new HashMap<>();
        resp.put("message", "Retrieve All Courses");
        resp.put("data", categoryList);
        resp.put("currentPage", coursePage.getNumber());
        resp.put("totalItems", coursePage.getTotalElements());
        resp.put("totalPages", coursePage.getTotalPages());
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


    /**
     * * Getting Course List
     * * API : http://localhost:8080/api/course/get-courses
     */
    @GetMapping("/get-courses")
    public ResponseEntity<Map<Object, Object>> getCourses(@RequestHeader("loggedInUser") String username){


        List<Course> courses = courseService.getCoursesList();

        if (courses == null)
            throw new IllegalStateException("Courses Not Available !");

        Map<Object, Object> resp = new HashMap<Object, Object>();

        resp.put("message", "Course Details Retrieved Successfully");
        resp.put("courses", courses);
        resp.put("username", username);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }



    
}
