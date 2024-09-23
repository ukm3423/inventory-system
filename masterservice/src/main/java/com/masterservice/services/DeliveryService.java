package com.masterservice.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masterservice.dto.OrderDetailsDTO;
import com.masterservice.handlers.Generator;
import com.masterservice.mapper.DeliveryRequest;
import com.masterservice.models.Category;
import com.masterservice.models.Delivery;
import com.masterservice.models.DeliveryDetails;
import com.masterservice.models.Order;
import com.masterservice.models.OrderDetails;
import com.masterservice.models.Product;
import com.masterservice.repository.CategoryRepository;
import com.masterservice.repository.DeliveryDetailsRepository;
import com.masterservice.repository.DeliveryRepository;
import com.masterservice.repository.OrderDetailsRepository;
import com.masterservice.repository.OrderRepository;
import com.masterservice.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepo;

    @Autowired
    private DeliveryDetailsRepository deliveryDetailsRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderDetailsRepository orderDetailsRepo;

    @Autowired
    private CategoryRepository catRepo;

    @Autowired
    private ProductRepository productRepo;


    public Object makeDelivery(DeliveryRequest req) {

        Order order = orderRepo.findByOrderNumber(req.getOrderCode()); 
        List<OrderDetails> orderDetailsList = orderDetailsRepo.findByOrder(order);
        if(order.getStatus() == 1){
            throw new IllegalStateException("Already delivered");
        }

        String invoiceNumber = Generator.generateInvoiceNumber();
        Delivery delivery = Delivery.builder()
                .orderDate(req.getOrderDate())
                .deliveryDate(LocalDate.now())
                .supplier(req.getSupplierName())
                .status(1)
                .orderNumber(req.getOrderCode())
                .invoiceNumber(invoiceNumber)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        delivery = deliveryRepo.save(delivery);

        List<OrderDetailsDTO> orderList = req.getOrderDetailsList();

        @SuppressWarnings("rawtypes")
        Iterator itr = orderList.iterator();

        while (itr.hasNext()) {
            OrderDetailsDTO od = (OrderDetailsDTO) itr.next();


            Category category = catRepo.findById(od.getCategoryId()).get();
            Product product = productRepo.findById(od.getProductId()).get();

            DeliveryDetails deliveryDetails = DeliveryDetails.builder()
                    .category(category)
                    .product(product)
                    .delivery(delivery)
                    .quantity(od.getQuantity())
                    .rate(od.getRate())
                    .status(1)
                    .updatedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build();
            deliveryDetailsRepo.save(deliveryDetails);
            // System.out.println(deliveryDetails);
        }

        // Update order status to delivered in order repository
        order.setDeliveryDate(delivery.getDeliveryDate());
        order.setInvoiceNumber(delivery.getInvoiceNumber());
        order.setStatus(1);
        orderRepo.save(order);

        // Update order details status to delivered in order repository
        @SuppressWarnings("rawtypes")
        Iterator iterator = orderDetailsList.iterator();
        while(iterator.hasNext()){
            OrderDetails od = (OrderDetails) iterator.next(); 
            od.setStatus(1);
        }

        return delivery;
    }
}
