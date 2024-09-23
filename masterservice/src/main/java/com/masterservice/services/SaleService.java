package com.masterservice.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masterservice.dto.SaleDetailsDTO;
import com.masterservice.handlers.Generator;
import com.masterservice.mapper.SaleRequest;
import com.masterservice.mapper.SaleResponse;
import com.masterservice.models.Category;
import com.masterservice.models.Product;
import com.masterservice.models.Sale;
import com.masterservice.models.SaleDetails;
import com.masterservice.repository.CategoryRepository;
import com.masterservice.repository.ProductRepository;
import com.masterservice.repository.SaleDetailsRepository;
import com.masterservice.repository.SaleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SaleService {
    
    @Autowired
    private SaleRepository saleRepo;

    @Autowired
    private SaleDetailsRepository saleDetailsRepo;

    @Autowired
    private CategoryRepository catRepo;

    @Autowired
    private ProductRepository productRepo;

    /**
     * Adds a Sale 
     * @param req
     * @return
     */
    public Sale addsale(SaleRequest req) {

        String billNumber = Generator.generateBillNumber();
        Sale sale = Sale.builder()
                .saleDate(req.getDate())
                .billDate(LocalDate.now())
                .contactNo(req.getContactNo())
                .customerName(req.getCustomerName())
                .status(1)
                .billNumber(billNumber)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sale = saleRepo.save(sale);
        List<SaleDetailsDTO> saleList = req.getSaleDetailsList();

        @SuppressWarnings("rawtypes")
        Iterator itr = saleList.iterator();

        while (itr.hasNext()) {
            SaleDetailsDTO od = (SaleDetailsDTO) itr.next();

            Category category = catRepo.findById(od.getCategoryId()).get();
            Product product = productRepo.findById(od.getProductId()).get();

            SaleDetails saleDetails = SaleDetails.builder()
                    .category(category)
                    .product(product)
                    .sale(sale)
                    .quantity(od.getQuantity())
                    .rate(od.getRate())
                    .status(1)
                    .updatedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build();
            saleDetailsRepo.save(saleDetails);
            System.out.println(saleDetails);
        }

        return sale;
    }


    /**
     * Get SALE details list
     * @return
     */
    public List<Sale> getAllSales() {
        return saleRepo.findAll();
    }


    /**
     * Get Sale details by ID
     * @param saleId
     * @return
     */
    public SaleResponse getSaleById(Long saleId) {

        Sale sale = saleRepo.findById(saleId).orElseThrow(() -> new IllegalStateException("Sale not found of id " + saleId));
        
        List<SaleDetails> saleDetailsList = saleDetailsRepo.findBySale(sale);


        List<SaleDetailsDTO> saleDetailsDTOList = saleDetailsList.stream()
                .map(saleDetails -> SaleDetailsDTO.builder()
                         .categoryId(saleDetails.getCategory().getId())
                         .categoryName(saleDetails.getCategory().getCategoryName())
                         .productId(saleDetails.getProduct().getId())
                         .productName(saleDetails.getProduct().getProductName())
                         .quantity(saleDetails.getQuantity())
                         .rate(saleDetails.getRate())
                         .build())
                .collect(Collectors.toList());

        SaleResponse saleResponse = SaleResponse.builder()
                .saleDate(sale.getSaleDate())
                .contactNo(sale.getContactNo())
                .customerName(sale.getCustomerName())
                .billNumber(sale.getBillNumber())
                .saleDetailsList(saleDetailsDTOList)
                .build();

        return saleResponse;
    }
    

    /**
     * GET OrderDetails for quantity
     * @param product
     * @return
     */
    public List<SaleDetails> getOrderDetailsByproduct(Product product) {

        List<SaleDetails> saleDetails = saleDetailsRepo.findByProductAndStatus(product, 1);

        return saleDetails;
    }

}
