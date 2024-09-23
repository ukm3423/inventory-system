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

import com.masterservice.dto.ProductDTO;
import com.masterservice.models.Product;
import com.masterservice.services.ProductService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    /**
     * * ===========================================================================
     * * ======================== Module : ProductController ======================
     * * ======================== Created By : Umesh Kumar =========================
     * * ======================== Created On : 29-06-2024 ==========================
     * * ===========================================================================
     * * | Code Status : On
     */
    @Autowired
    private ProductService productService;;

    /**
     * * Add a New Product
     * * API : localhost:8080/masterservice/api/products/add
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Validated @RequestBody ProductDTO req,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Construct error response
            Map<Object, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("error", "Validation Error");
            errorResponse.put("message",
                    "Validation failed for object 'ProductRequest'. Error count: " + bindingResult.getErrorCount());

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

        Product checkProductCodeExisting = productService.searchByProductCode(req.getProductCode());
        if (checkProductCodeExisting != null)
            throw new IllegalStateException("Product Code already exists !!");

        Product checkProductExisting = productService.searchByProductName(req.getProductName());
        if (checkProductExisting != null)
            throw new IllegalStateException("Product Name already exists !!");

        Map<Object, Object> resp = new HashMap<>();

        Product product = productService.saveProduct(req);

        resp.put("data", product);
        resp.put("message", "Product Added Successfully");
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /**
     * * Update Product BY Id
     * * API : http://localhost:8080/masterservice/api/products/update/1
     * 
     * @param productId
     * @param req
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long productId,
            @Valid @RequestBody ProductDTO req,
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

        Product checkIdExisting = productService.searchByProductCode(req.getProductCode());
        if (checkIdExisting != null && !checkIdExisting.getId().equals(productId)) {
            throw new IllegalStateException("Product Code already exists !");
        }

        Product checkProductExisting = productService.searchByProductName(req.getProductName());
        if (checkProductExisting != null && !checkProductExisting.getId().equals(productId)) {
            throw new IllegalStateException("Product Name already exists !");
        }
        Product Product = productService.updateProduct(productId, req);
        if (Product == null)
            throw new IllegalStateException("Product Not Found of Id : " + productId);

        Map<Object, Object> res = new HashMap<>();
        res.put("message", "Category Updated Successfully");
        res.put("data", Product);
        res.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    /**
     * * Delete Product By Id
     * * API : http://localhost:8080/masterservice/api/products/delete/1
     * 
     * @param id
     * @return
     */
    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        Boolean isdeleted = productService.deleteProductById(id);
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
     * * Get Product By Id
     * * API : http://localhost:8082/masterservice/api/products/get/2
     * 
     * @param productId
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Map<Object, Object>> getCategoryById(@PathVariable("id") Long productId) {

        Product Product = productService.getProductById(productId);

        if (Product == null)
            throw new IllegalStateException("Product Details Not Found of Id : " + productId);

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Product Details Retrieved Successfully");
        resp.put("data", Product);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }



    /**
     * * Get All Product Details
     * * API : http://localhost:8082/masterservice/api/products/get-list
     * 
     * @param offset
     * @param limit
     * @return
     */
    @GetMapping("/get-list")
    public ResponseEntity<?> getproductList(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "5") int limit) {

        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").descending());
        Page<Product> productPage = productService.getProductList(pageable);

        List<Product> productList = productPage.getContent();

        Map<Object, Object> resp = new HashMap<>();
        resp.put("message", "Retrieve All Products");
        resp.put("data", productList);
        resp.put("currentPage", productPage.getNumber());
        resp.put("totalItems", productPage.getTotalElements());
        resp.put("totalPages", productPage.getTotalPages());
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    /**
     * * Getting Product List
     * * API : http://localhost:8082/masterservice/api/products/get-products
     */
    @GetMapping("/get-products")
    public ResponseEntity<?> getProducts() {

        List<Product> Products = productService.getProductList();

        if (Products == null)
            throw new IllegalStateException("Products Not Available !");

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Product Details Retrieved Successfully");
        resp.put("data", Products);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }

    /**
     * * Getting Product List
     * * API : http://localhost:8082/masterservice/api/products/get-products
     */
    @GetMapping("/get-products/{categoryId}")
    public ResponseEntity<?> getProductByCategoryId(@PathVariable(name="categoryId") Long categoryId) {

        List<Product> products = productService.getCategoryWiseProductList(categoryId);

        if (products == null)
            throw new IllegalStateException("Products Not Available !");

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Product Details Retrieved Successfully");
        resp.put("data", products);
        resp.put("name", "Category Wise Product List.");
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }


    /**
     * * Getting Product List
     * * API : http://localhost:8082/masterservice/api/products/get-products
     */
    @GetMapping("/get-delivered-products/{categoryId}")
    public ResponseEntity<?> getDeliveredProductByCategoryId(@PathVariable(name="categoryId") Long categoryId) {

        Set<Product> products = productService.getDeliveredProductList(categoryId);

        if (products == null)
            throw new IllegalStateException("Products Not Available !");

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Product Details Retrieved Successfully");
        resp.put("data", products);
        resp.put("name", "Category Wise Product List.");
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }


}
