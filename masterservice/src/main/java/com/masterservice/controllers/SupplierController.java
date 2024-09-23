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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masterservice.dto.SupplierDTO;
import com.masterservice.models.Supplier;
import com.masterservice.services.SupplierService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/supplier")
public class SupplierController {

    /**
     * * ===========================================================================
     * * ======================== Module : SupplierController ======================
     * * ======================== Created By : Umesh Kumar =========================
     * * ======================== Created On : 30-06-2024 ==========================
     * * ===========================================================================
     * * | Code Status : On
     */
    @Autowired
    private SupplierService supplierService;;

    /**
     * * Add a New Supplier 
     * * API : localhost:8080/masterservice/api/supplier/add
     */
    @PostMapping("/add")
    public ResponseEntity<?> addSupplier(@Validated @RequestBody SupplierDTO req,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Construct error response
            Map<Object, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("error", "Validation Error");
            errorResponse.put("message",
                    "Validation failed for object 'SupplierRequest'. Error count: " + bindingResult.getErrorCount());

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

        Supplier checkSupplierEmail = supplierService.searchBySupplierEmail(req.getEmailAddress());
        if (checkSupplierEmail != null)
            throw new IllegalStateException("Email-Id already exists !!");

        Supplier checkSupplierPhone = supplierService.searchBySupplierPhone(req.getPhone());
        if (checkSupplierPhone != null)
            throw new IllegalStateException("Phone number already exists !!");

        Map<Object, Object> resp = new HashMap<>();

        Supplier Supplier = supplierService.saveSupplier(req);

        resp.put("data", Supplier);
        resp.put("message", "Supplier Added Successfully");
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /**
     * * Update Supplier BY Id
     * * API : http://localhost:8080/masterservice/api/supplier/update/1
     * 
     * @param SupplierId
     * @param req
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable("id") Long SupplierId,
            @Valid @RequestBody SupplierDTO req,
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

        Supplier checkIdExisting = supplierService.searchBySupplierEmail(req.getEmailAddress());
        if (checkIdExisting != null && !checkIdExisting.getId().equals(SupplierId)) {
            throw new IllegalStateException("Supplier Code already exists !");
        }

        Supplier checkSupplierPhone = supplierService.searchBySupplierPhone(req.getPhone());
        if (checkSupplierPhone != null && !checkSupplierPhone.getId().equals(SupplierId)) {
            throw new IllegalStateException("Supplier Name already exists !");
        }
        Supplier Supplier = supplierService.updateSupplier(SupplierId, req);
        if (Supplier == null)
            throw new IllegalStateException("Supplier Not Found of Id : " + SupplierId);

        Map<Object, Object> res = new HashMap<>();
        res.put("message", "Category Updated Successfully");
        res.put("data", Supplier);
        res.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    /**
     * * Delete Supplier By Id
     * * API : http://localhost:8080/masterservice/api/supplier/delete/1
     * 
     * @param id
     * @return
     */
    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {

        Boolean isdeleted = supplierService.deleteSupplierById(id);
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
     * * Get Supplier By Id
     * * API : http://localhost:8082/masterservice/api/Suppliers/get/2
     * 
     * @param SupplierId
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Map<Object, Object>> getSupplierBySupplierId(@PathVariable("id") Long SupplierId) {

        Supplier supplier = supplierService.getSupplierById(SupplierId);

        if (supplier == null)
            throw new IllegalStateException("Supplier Details Not Found of Id : " + SupplierId);

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Supplier Details Retrieved Successfully");
        resp.put("data", supplier);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    /**
     * * Get All Supplier Details
     * * API : http://localhost:8082/masterservice/api/Suppliers/get-list
     * 
     * @param offset
     * @param limit
     * @return
     */
    @GetMapping("/get-list")
    public ResponseEntity<?> getSupplierList(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "5") int limit) {

        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").descending());
        Page<Supplier> SupplierPage = supplierService.getSupplierList(pageable);

        List<Supplier> SupplierList = SupplierPage.getContent();

        Map<Object, Object> resp = new HashMap<>();
        resp.put("message", "Retrieve All Suppliers");
        resp.put("data", SupplierList);
        resp.put("currentPage", SupplierPage.getNumber());
        resp.put("totalItems", SupplierPage.getTotalElements());
        resp.put("totalPages", SupplierPage.getTotalPages());
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    /**
     * * Getting Supplier List
     * * API : http://localhost:8082/masterservice/api/Suppliers/get-Suppliers
     */
    @GetMapping("/get-suppliers")
    public ResponseEntity<?> getSuppliers() {

        List<Supplier> Suppliers = supplierService.getSupplierList();

        if (Suppliers == null)
            throw new IllegalStateException("Suppliers Not Available !");

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Supplier Details Retrieved Successfully");
        resp.put("Suppliers", Suppliers);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }

}
