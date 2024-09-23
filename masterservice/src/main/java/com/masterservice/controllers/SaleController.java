package com.masterservice.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterservice.mapper.SaleRequest;
import com.masterservice.mapper.SaleResponse;
import com.masterservice.models.Sale;
import com.masterservice.services.SaleService;

@RestController
@CrossOrigin
@RequestMapping("/sale")
public class SaleController {
    /**
     * * ===========================================================================
     * * ======================== Module : SaleController ==========================
     * * ======================== Created By : Umesh Kumar =========================
     * * ======================== Created On : 17-07-2024 ==========================
     * * ===========================================================================
     * * | Code Status : On
     */

    @Autowired
    private SaleService saleService;

    /*
     * Place a New Order
     */
    @PostMapping("/add")
    public ResponseEntity<?> placeOrder(@Validated @RequestBody SaleRequest req, BindingResult bindingResult) {

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

        Sale obj = saleService.addsale(req);

        Map<String, Object> resp = new HashMap<>();
        resp.put("data", obj);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


    /**
     * Get SALE List
     * @return
     */
    @GetMapping("/get-sale-list")
    public ResponseEntity<?> getOrderList() {

        List<Sale> saleList = saleService.getAllSales();

        return ResponseEntity.status(HttpStatus.OK).body(saleList);
    }


    /**
     * * Get Order By Id
     * * API : http://localhost:8082/masterservice/api/order/get/2
     * 
     * @param saleId
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long saleId) {

        SaleResponse sale = saleService.getSaleById(saleId);

        if (sale == null)
            throw new IllegalStateException("Sale Details Not Found of Id : " + saleId);

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Sale Details Retrieved Successfully");
        resp.put("data", sale);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


}
