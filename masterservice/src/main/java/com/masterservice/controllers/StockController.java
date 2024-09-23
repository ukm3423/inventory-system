package com.masterservice.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterservice.mapper.CurrentStockFormatter;
import com.masterservice.models.OrderDetails;
import com.masterservice.models.Product;
import com.masterservice.models.SaleDetails;
import com.masterservice.services.OrderService;
import com.masterservice.services.ProductService;
import com.masterservice.services.SaleService;

@RestController
@CrossOrigin
@RequestMapping("/stock")
public class StockController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SaleService saleService;

    @GetMapping("/get-data")
    public ResponseEntity<?> getCurrentStockReport() {
        List<CurrentStockFormatter> csfList = new ArrayList<>();

        List<Product> products = productService.getProductList();

        Iterator<Product> itr = products.iterator();

        while (itr.hasNext()) {
            Product product = itr.next();

            CurrentStockFormatter csf = new CurrentStockFormatter();

            csf.setCategoryName(product.getCategory().getCategoryName());
            csf.setProductName(product.getProductName());

            Integer purchaseQty = 0;
            Integer saleQty = 0;
            Integer availableQty = 0;

            List<OrderDetails> pdList = orderService.getOrderDetailsByproduct(product);
            Iterator<OrderDetails> i = pdList.iterator();
            while (i.hasNext()) {
                OrderDetails od = i.next();
                purchaseQty = purchaseQty + od.getQuantity();
            }

            List<SaleDetails> sdList = saleService.getOrderDetailsByproduct(product);
            Iterator<SaleDetails> i2 = sdList.iterator();
            while (i2.hasNext()) {
                SaleDetails sd = i2.next();
                saleQty = saleQty + sd.getQuantity();
            }

            availableQty = purchaseQty - saleQty;
            csf.setPurchaseQty(purchaseQty);
            csf.setSaleQty(saleQty);
            csf.setAvailableQty(availableQty);
            csfList.add(csf);    

        }

        return ResponseEntity.status(HttpStatus.OK).body(csfList);
    }
}
