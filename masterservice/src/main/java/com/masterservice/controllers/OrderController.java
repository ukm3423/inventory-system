package com.masterservice.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterservice.mapper.OrderResponse;
import com.masterservice.mapper.PlaceOrderRequest;
import com.masterservice.models.Order;
import com.masterservice.services.OrderService;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*
     * Place a New Order 
     */
    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderRequest req) {

        Object obj = orderService.addOrder(req);

        Map<String, Object> resp = new HashMap<>();
        resp.put("data", obj);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/get-order-list")
    public ResponseEntity<?> getOrderList(){
        
        List<Order> orderList = orderService.getAllOrders();

        return ResponseEntity.status(HttpStatus.OK).body(orderList);
    }

    /**
     * * Get Order By Id
     * * API : http://localhost:8082/masterservice/api/order/get/2
     * 
     * @param OrderId
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long OrderId) {

        OrderResponse order = orderService.getOrderById(OrderId);

        if (order == null)
            throw new IllegalStateException("Order Details Not Found of Id : " + OrderId);

        Map<Object, Object> resp = new HashMap<>();

        resp.put("message", "Order Details Retrieved Successfully");
        resp.put("data", order);
        resp.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

}
