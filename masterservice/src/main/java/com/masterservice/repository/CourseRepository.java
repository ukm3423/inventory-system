package com.masterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterservice.models.Course;

public interface CourseRepository extends JpaRepository<Course , Long>{
    
    public Course findByCourseNameIgnoreCase(String courseName);

    public Course findByCourseIdIgnoreCase(String courseId);

}
