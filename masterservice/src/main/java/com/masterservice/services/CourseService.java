package com.masterservice.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masterservice.dto.CourseDTO;
import com.masterservice.models.Course;
import com.masterservice.repository.CourseRepository;



@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepo;

    /**
     * * Adding a new course
     * 
     * @param req
     * @return
     */
    public Course addCourse(CourseDTO req) {
        Course course = Course.builder()
                .courseName(req.getCourseName())
                .duration(req.getCourseDuration())
                .fee(req.getCourseFee())
                .status(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .courseId(req.getCourseId())
                .build();

        courseRepo.save(course);
        return course;
    }

    
    /**
     * Searching Course by CourseName
     * @param courseName
     * @return
     */
    public Course searchByCourseName(String courseName) {
        return courseRepo.findByCourseNameIgnoreCase(courseName);
    }


    /**
     * * Searching for course by course ID
     * @param courseId
     * @return
     */
    public Course searchByCourseId(String courseId) {
        return courseRepo.findByCourseIdIgnoreCase(courseId);
    }

    /**
     * * Update Course Details By Id
     * 
     * @param courseId
     * @param req
     * @return
     */
    public Course updateCourseById(Long courseId, CourseDTO req) {

        Course course = null;
        Optional<Course> op = courseRepo.findById(courseId);
        if (op.isPresent()) {
            course = op.get();
            course.setCourseId(req.getCourseId());
            course.setCourseName(req.getCourseName());
            course.setDuration(req.getCourseDuration());
            course.setFee(req.getCourseFee());
            course.setUpdatedAt(LocalDateTime.now());

            courseRepo.save(course);
        }
        return course;

    }

    /**
     * * Delete Course By Id
     * 
     * @param id
     * @return
     */
    public Boolean deleteCourseById(Long id) {

        Optional<Course> op = courseRepo.findById(id);

        if (op.isPresent()) {
            Course course = op.get();
            course.setStatus(!course.getStatus());
            courseRepo.save(course);
            return true;
        } else {
            return false;
        }

    }


    /**
     * * Get Course Details by Course ID
     * @param courseId
     * @return
     */
    public Course getCourseById(Long courseId) {

        Optional<Course> op = courseRepo.findById(courseId);
        if(op.isPresent()) {
            return op.get();
        }   
        return null;
    }


    public Page<Course> getCoursesList(Pageable pageable) {
        return courseRepo.findAll(pageable);
    }


    public List<Course> getCoursesList() {
        // Creating a Sort object to specify the sorting criteria
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
    
        // Returning the list of courses sorted by descending order of ID
        return courseRepo.findAll(sort);
    }
}
