package com.masterservice.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masterservice.dto.CategoryDTO;
import com.masterservice.models.Category;
import com.masterservice.services.CategoryService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {
    

    /**
     * * ===========================================================================
     * * ======================== Module : CategoryController ======================
     * * ======================== Created By : Umesh Kumar =========================
     * * ======================== Created On : 28-06-2024 ==========================
     * * ===========================================================================
     * * | Code Status : On
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * * Add a New Category
     * * API : http://localhost:8082/masterservice/api/category/add
     */
    @PostMapping("/add")
    public ResponseEntity<Map<Object, Object>> addCategory(@Validated @RequestBody CategoryDTO req,
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


        Category checkCategoryExisting = categoryService.searchByCategoryName(req.getCategoryName());
        if (checkCategoryExisting != null)
            throw new IllegalStateException("Category Name already exists !!");

        Map<Object, Object> resp = new HashMap<>();

        Category category = categoryService.saveCategory(req);

        resp.put("data", category);
        resp.put("message", "Category Added Successfully");
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /**
     * * Update Course BY Id
     * * API : http://localhost:8082/masterservice/api/category/update/1
     * 
     * @param courseId
     * @param req
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<Object, Object>> updateCourse(@PathVariable("id") Long categoryId,
            @Valid @RequestBody CategoryDTO req,
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

        Category checkCourseExisting = categoryService.searchByCategoryName(req.getCategoryName());
        if (checkCourseExisting != null && !checkCourseExisting.getId().equals(categoryId)) {
            throw new IllegalStateException("Category Name already exists !");
        }

        Category category = categoryService.updateCategoryById(categoryId, req);

        if (category == null)
            throw new IllegalStateException("Course Not Found of Id : " + categoryId);

        Map<Object, Object> res = new HashMap<>();
        res.put("message", "Category Updated Successfully");
        res.put("data", category);
        res.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    /**
     * * Delete Course By Id
     * * API : http://localhost:8082/masterservice/api/category/delete/1
     * 
     * @param id
     * @return
     */
    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<Object, Object>> deleteCategory(@PathVariable Long id) {

        Boolean isdeleted = categoryService.deleteCategoryById(id);
        Map<Object, Object> resp = new HashMap<>();

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
     * * API : http://localhost:8082/masterservice/api/category/get/2
     * 
     * @param courseId
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Map<Object, Object>> getCategoryById(@PathVariable("id") Long categoryId) {

        Category category = categoryService.getCategoryById(categoryId);

        if (category == null)
            throw new IllegalStateException("Category Not Found of Id : " + categoryId);

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Category Details Retrieved Successfully");
        resp.put("data", category);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    /**
     * * Get All Course Details
     * * API : http://localhost:8082/masterservice/api/category/get-list
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
        Page<Category> categoryPage = categoryService.getCategoryList(pageable);
        List<Category> categoryList = categoryPage.getContent();

        Map<Object, Object> resp = new HashMap<>();
        resp.put("message", "Retrieve All Courses");
        resp.put("data", categoryList);
        resp.put("currentPage", categoryPage.getNumber());
        resp.put("totalItems", categoryPage.getTotalElements());
        resp.put("totalPages", categoryPage.getTotalPages());
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


    /**
     * * Getting Course List
     * * API : http://localhost:8082/masterservice/api/category/get-categories
     */
    @GetMapping("/get-categories")
    public ResponseEntity<Map<Object, Object>> getCourses(){


        List<Category> courses = categoryService.getCategoryList();

        if (courses == null)
            throw new IllegalStateException("Category Not Available !");

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Category Details Retrieved Successfully");
        resp.put("data", courses);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }


    /**
     * * Getting Course List
     * * API : http://localhost:8082/masterservice/api/category/get-categories
     */
    @GetMapping("/get-categories-list")
    public ResponseEntity<?> getCategoryList(){


        Set<Category> courses = categoryService.getDeliveredCategoryList();

        if (courses == null)
            throw new IllegalStateException("Category Not Available !");

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Category Details Retrieved Successfully");
        resp.put("data", courses);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }














    
    // @GetMapping("/test")
    // public String test(){
    //     return "test pass";
    // }

}
