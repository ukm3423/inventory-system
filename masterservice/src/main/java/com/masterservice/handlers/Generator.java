package com.masterservice.handlers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.masterservice.repository.DeliveryRepository;
import com.masterservice.repository.OrderRepository;
import com.masterservice.repository.SaleRepository;

@Component
public class Generator {


    private static OrderRepository orderRepo;
    private static DeliveryRepository deliveryRepo; 
    private static SaleRepository saleRepo;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepo, DeliveryRepository deliveryRepo, SaleRepository saleRepo) {
        Generator.orderRepo = orderRepo;
        Generator.deliveryRepo = deliveryRepo;
        Generator.saleRepo = saleRepo;
    }
    
    // ================================= Order Number Generator =================================
    public static String generateOrderNumber() {
        String orderNumber;
        do {
            orderNumber = generateRandomOrderNumber();
        } while (!isorderNumberUnique(orderNumber));
        return orderNumber;
    }

    private static String generateRandomOrderNumber() {
        Random random = new Random();
        // You can adjust the length of the random number as needed
        String randomNumber = String.format("%010d", random.nextInt(1000000));
        return "ODR" + randomNumber;
    }

    private static boolean isorderNumberUnique(String orderNumber) {
        return orderRepo.findByOrderNumber(orderNumber) == null;
    }

    // ====================================  Invoice Number Generator =================================
    public static String generateInvoiceNumber() {
        String invoiceNumber;
        do {
            invoiceNumber = generateRandomInvoiceNumber();
        } while (!isInvoiceNumberUnique(invoiceNumber));
        return invoiceNumber;
    }

    private static String generateRandomInvoiceNumber() {
        Random random = new Random();
        // You can adjust the length of the random number as needed
        String randomNumber = String.format("%010d", random.nextInt(1000000));
        return "INV" + randomNumber;
    }

    private static boolean isInvoiceNumberUnique(String invoiceNumber) {
        return deliveryRepo.findByInvoiceNumber(invoiceNumber) == null;
    }


    //  =================================== Bill Number Generator =================================
    public static String generateBillNumber() {
        String billNumber; 
        do {
            billNumber = generateRandomBillNumber();
        } while (!isBillNumberUnique(billNumber));
        return billNumber;
    }

    private static String generateRandomBillNumber() {
        Random random = new Random();
        // You can adjust the length of the random number as needed
        String randomNumber = String.format("%010d", random.nextInt(1000000));
        return "BIL" + randomNumber;
    }

    private static boolean isBillNumberUnique(String billNumber) {
        return saleRepo.findByBillNumber(billNumber) == null;
    }

}
