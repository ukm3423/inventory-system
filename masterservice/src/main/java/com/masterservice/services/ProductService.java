package com.masterservice.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.masterservice.dto.ProductDTO;
import com.masterservice.models.Category;
import com.masterservice.models.DeliveryDetails;
import com.masterservice.models.Product;
import com.masterservice.repository.DeliveryDetailsRepository;
import com.masterservice.repository.ProductRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private DeliveryDetailsRepository deliveryRepo;

    @Autowired
    private CategoryService catService;

    public Product searchByProductName(String productName) {
        return productRepo.findByProductNameIgnoreCase(productName);
    }

    public Product searchByProductCode(String productCode) {
        return productRepo.findByProductCodeIgnoreCase(productCode);
    }

    public Product saveProduct(ProductDTO req) {

        Category category = catService.getCategoryById(req.getCategoryId());

        if (category == null) {
            // Handle the case where category is not found or invalid
            throw new IllegalArgumentException("Invalid categoryId: " + req.getCategoryId());
        }

        Product product = Product.builder()
               .productName(req.getProductName())
               .productCode(req.getProductCode())
               .price(req.getPrice())
               .category(category)
               .status(1)
               .createdAt(LocalDateTime.now())
               .updatedAt(LocalDateTime.now())
               .build();

        productRepo.save(product);
        return product;
    }

    public Product updateProduct(Long productId, @Valid ProductDTO req) {
        Product product = null;
        Category category = catService.getCategoryById(req.getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException("Invalid categoryId: " + req.getCategoryId());
        }
        Optional<Product> op = productRepo.findById(productId);
        if (op.isPresent()) {
            product = op.get();
            product.setPrice(req.getPrice());
            product.setProductName(req.getProductName());
            product.setProductCode(req.getProductCode());
            product.setCategory(category);
            product.setUpdatedAt(LocalDateTime.now());
            productRepo.save(product);
        }
        return product;
    }


    public Boolean deleteProductById(Long id) {
        Optional<Product> op = productRepo.findById(id);

        if (op.isPresent()) {
            Product product = op.get();
            if(product.getStatus() == 0){
                product.setStatus(1);
            }else{
                product.setStatus(0);
            }
            
            productRepo.save(product);
            return true;
        } else {
            return false;
        }
    }

    public Product getProductById(Long productId) {
        Optional<Product> op = productRepo.findById(productId);
        Product product = null;
        if (op.isPresent()) {
            product = op.get();
    }
        return product;
    }

    public Page<Product> getProductList(Pageable pageable) {
        return productRepo.findAll(pageable);

    }

    public List<Product> getProductList() {
        // Creating a Sort object to specify the sorting criteria
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
    
        // Returning the list of courses sorted by descending order of ID
        return productRepo.findAll(sort);
    }

    public List<Product> getCategoryWiseProductList(Long categoryId) {
        
        List<Product> products = productRepo.findByCategoryIdAndStatus(categoryId, 1);

        return products;
    }

    public Set<Product> getDeliveredProductList(Long categoryId) {

        Category category = catService.getCategoryById(categoryId);
        List<DeliveryDetails> products = deliveryRepo.findByCategory(category);
        Set<Product> productList = new HashSet<>();
        Iterator it = products.iterator();
        while (it.hasNext()) {
            DeliveryDetails dd = (DeliveryDetails) it.next();
            productList.add(dd.getProduct());
        }
        return productList;

    }
    

}
