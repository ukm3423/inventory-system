package com.masterservice.services;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masterservice.dto.OrderDetailsDTO;
import com.masterservice.handlers.Generator;
import com.masterservice.mapper.OrderResponse;
import com.masterservice.mapper.PlaceOrderRequest;
import com.masterservice.models.Category;
import com.masterservice.models.Order;
import com.masterservice.models.OrderDetails;
import com.masterservice.models.Product;
import com.masterservice.models.Supplier;
import com.masterservice.repository.CategoryRepository;
import com.masterservice.repository.OrderDetailsRepository;
import com.masterservice.repository.OrderRepository;
import com.masterservice.repository.ProductRepository;
import com.masterservice.repository.SupplierRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderDetailsRepository orderDetailsRepo;

    @Autowired
    private CategoryRepository catRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private SupplierRepository suppRepo;

    /**
     * Place a New Order
     * @param req
     * @return
     */
    public Object addOrder(PlaceOrderRequest req) {

        Supplier supplier = suppRepo.findById(req.getSupplierId()).get();
        String orderNumber = Generator.generateOrderNumber();
        Order order = Order.builder()
                .orderDate(req.getDate())
                .supplier(supplier)
                .status(0)
                .orderNumber(orderNumber)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        order = orderRepo.save(order);

        List<OrderDetailsDTO> orderList = req.getProductDetailsList();

        @SuppressWarnings("rawtypes")
        Iterator itr = orderList.iterator();

        while (itr.hasNext()) {
            OrderDetailsDTO od = (OrderDetailsDTO) itr.next();

            Category category = catRepo.findById(od.getCategoryId()).get();
            Product product = productRepo.findById(od.getProductId()).get();

            OrderDetails orderDetails = OrderDetails.builder()
                    .category(category)
                    .product(product)
                    .order(order)
                    .quantity(od.getQuantity())
                    .rate(od.getRate())
                    .status(0)
                    .updatedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build();
            orderDetailsRepo.save(orderDetails);
            System.out.println(orderDetails);
        }

        return order;
    }

    /**
     * Get All Order Details
     * @return
     */
    public List<Order> getAllOrders() {

        return orderRepo.findAll();

    }


    /**
     * Get Order By Order Id
     * @param orderId
     * @return
     */
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepo.findById(orderId).orElseThrow(() -> new IllegalStateException("Order not found of id " + orderId));
        
        List<OrderDetails> orderDetailsList = orderDetailsRepo.findByOrder(order);

        List<OrderDetailsDTO> orderDetailsDTOList = orderDetailsList.stream()
                .map(orderDetails -> OrderDetailsDTO.builder()
                        .categoryId(orderDetails.getCategory().getId())
                        .categoryName(orderDetails.getCategory().getCategoryName())
                        .productId(orderDetails.getProduct().getId())
                        .productName(orderDetails.getProduct().getProductName())
                        .quantity(orderDetails.getQuantity())
                        .rate(orderDetails.getRate())
                        .build())
                .collect(Collectors.toList());

        OrderResponse orderResponse = OrderResponse.builder()
                .supplierName(order.getSupplier().getSupplierName())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getOrderDate())
                .orderDetailsList(orderDetailsDTOList)
                .build();

        return orderResponse;
    }

    /**
     * GET OrderDetails for quantity
     * @param product
     * @return
     */
    public List<OrderDetails> getOrderDetailsByproduct(Product product) {

        List<OrderDetails> orderDetails = orderDetailsRepo.findByProductAndStatus(product, 1);
        

        return orderDetails;
    }

    public List<OrderDetails> getQuantityByProductId(Long productId){

        Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalStateException("Product not found of id " + productId));

        List<OrderDetails> orderDetails = orderDetailsRepo.findByProductAndStatus(product, 1);


        return orderDetails;
    }



}
