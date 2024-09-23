package com.masterservice.controllers.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterservice.mapper.PurchaseReport;
import com.masterservice.mapper.PurchaseReportDetails;
import com.masterservice.models.Order;
import com.masterservice.models.OrderDetails;
import com.masterservice.repository.OrderDetailsRepository;
import com.masterservice.repository.OrderRepository;
import com.masterservice.services.OrderService;

@RestController
@CrossOrigin
@RequestMapping("/reports")
public class PurchaseReportController {

   /**
    * * ===========================================================================
    * * ======================== Module : PurchaseReportController ================
    * * ======================== Created By : Umesh Kumar =========================
    * * ======================== Created On : 15-07-2024 ==========================
    * * ===========================================================================
    * * | Code Status : On
    */

   @Autowired
   private OrderService orderService;

   @Autowired
   private OrderDetailsRepository odRepo;

   @Autowired
   private OrderRepository orderRepo;

   @GetMapping("/get-purchase-report")
   public ResponseEntity<?> getPurchaseReport() {

      List<PurchaseReport> purchaseReport = new ArrayList<>();
      List<Order> allOrders = orderService.getAllOrders();

      List<Order> filteredOrders = allOrders.stream()
            .filter(order -> order.getStatus() == 1)
            .collect(Collectors.toList());

      @SuppressWarnings("rawtypes")
      Iterator orderIterator = filteredOrders.iterator();

      while (orderIterator.hasNext()) {
         Order order = (Order) orderIterator.next();
         PurchaseReport pr = new PurchaseReport();

         pr.setId(order.getId());
         pr.setDeliveryDate(order.getDeliveryDate());
         pr.setOrderDate(order.getOrderDate());
         pr.setOrderNo(order.getOrderNumber());
         pr.setInvoiceNo(order.getInvoiceNumber());
         // pr.setAmount(900000D);
         pr.setSupplier(order.getSupplier().getSupplierName());
         Double amount = 0D;
         List<OrderDetails> orderDetails = odRepo.findByOrder(order);
         List<PurchaseReportDetails> purchaseDetailsList = new ArrayList<>();

         Iterator orderDetailsList = orderDetails.iterator();
         while (orderDetailsList.hasNext()) {
            OrderDetails od = (OrderDetails) orderDetailsList.next();
            PurchaseReportDetails prd = PurchaseReportDetails.builder()
                  .categoryName(od.getCategory().getCategoryName())
                  .productName(od.getProduct().getProductName())
                  .rate(od.getRate())
                  .quantity(od.getQuantity())
                  .totalAmount(od.getRate() * od.getQuantity())
                  .build();
            purchaseDetailsList.add(prd);
            amount = amount + prd.getTotalAmount();
         }

         pr.setPurchaseDetailsList(purchaseDetailsList);
         pr.setAmount(amount);
         purchaseReport.add(pr);

         orderIterator.remove(); // avoids a ConcurrentModificationException
      }

      Map<Object, Object> response = new HashMap<>();
      response.put("data", purchaseReport);

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }




   /**
    * Get Purchase Report Details By Order Number
    * @param orderNo
    * @return
    */
   @GetMapping("/get-purchase-report/{orderNo}")
   public ResponseEntity<?> getPurchaseReportByOrderNo(@PathVariable String orderNo) {

      Order order = orderRepo.findByOrderNumber(orderNo);
      if (order == null) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
      }

      PurchaseReport pr = new PurchaseReport();

      pr.setId(order.getId());
      pr.setDeliveryDate(order.getDeliveryDate());
      pr.setOrderDate(order.getOrderDate());
      pr.setOrderNo(order.getOrderNumber());
      pr.setInvoiceNo(order.getInvoiceNumber());
      pr.setSupplier(order.getSupplier().getSupplierName());

      List<OrderDetails> orderDetails = odRepo.findByOrder(order);
      List<PurchaseReportDetails> purchaseDetailsList = new ArrayList<>();
      Double amount = 0D;
      Iterator orderDetailsList = orderDetails.iterator();
      while (orderDetailsList.hasNext()) {
         OrderDetails od = (OrderDetails) orderDetailsList.next();
         PurchaseReportDetails prd = PurchaseReportDetails.builder()
               .categoryName(od.getCategory().getCategoryName())
               .productName(od.getProduct().getProductName())
               .rate(od.getRate())
               .quantity(od.getQuantity())
               .totalAmount(od.getRate() * od.getQuantity())
               .build();
         purchaseDetailsList.add(prd);
         amount = amount + prd.getTotalAmount();
      }
      pr.setAmount(amount);
      pr.setPurchaseDetailsList(purchaseDetailsList);
      Map<Object, Object> response = new HashMap<>();
      response.put("data", pr);

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }
}
