package com.masterservice.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterservice.mapper.DeliveryRequest;
import com.masterservice.services.DeliveryService;

@RestController
@CrossOrigin
@RequestMapping("/delivery")
public class DeliveryController {

    /**
     * * ===========================================================================
     * * ======================== Module : DeliveryController ======================
     * * ======================== Created By : Umesh Kumar =========================
     * * ======================== Created On : 10-07-2024 ==========================
     * * ===========================================================================
     * * | Code Status : On
     */


    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/make-delivery")
    public ResponseEntity<?> placeOrder(@RequestBody DeliveryRequest req) {

        Object obj = deliveryService.makeDelivery(req);

        Map<String, Object> resp = new HashMap<>();
        resp.put("data", obj);
        resp.put("message", "Delivery Successfully");
        resp.put("status", true);
     

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

   

}
