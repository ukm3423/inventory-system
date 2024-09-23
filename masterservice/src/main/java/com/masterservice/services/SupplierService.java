package com.masterservice.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.masterservice.dto.SupplierDTO;
import com.masterservice.models.Supplier;
import com.masterservice.repository.SupplierRepository;

import jakarta.validation.Valid;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepo;

    public Supplier searchBySupplierEmail(String emailAddress) {
        return supplierRepo.findByEmailAddress(emailAddress);
    }

    public Supplier searchBySupplierPhone(Long phone) {
        return supplierRepo.findByPhone(phone);
    }

    public Supplier saveSupplier(SupplierDTO req) {

        Supplier supplier = Supplier.builder()
               .supplierName(req.getSupplierName())
               .phone(req.getPhone())
               .emailAddress(req.getEmailAddress())
               .address(req.getAddress())
               .status(1)
               .createdAt(LocalDateTime.now())
               .updatedAt(LocalDateTime.now())
               .build();

        supplierRepo.save(supplier);
        return supplier;
    }

    public Supplier updateSupplier(Long supplierId, @Valid SupplierDTO req) {
        Supplier supplier = null;
        Optional<Supplier> op = supplierRepo.findById(supplierId);
        if (op.isPresent()) {
            supplier = op.get();
            supplier.setAddress(req.getAddress());
            supplier.setSupplierName(req.getSupplierName());
            supplier.setPhone(req.getPhone());
            supplier.setEmailAddress(req.getEmailAddress());
            supplier.setUpdatedAt(LocalDateTime.now());
            supplierRepo.save(supplier);
        }
        return supplier;
    }

    public Boolean deleteSupplierById(Long id) {
        Optional<Supplier> op = supplierRepo.findById(id);

        if (op.isPresent()) {
            Supplier supplier = op.get();
            if(supplier.getStatus() == 0){
                supplier.setStatus(1);
            }else{
                supplier.setStatus(0);
            }
            
            supplierRepo.save(supplier);
            return true;
        } else {
            return false;
        }
    }

    public Supplier getSupplierById(Long supplierId) {
        Optional<Supplier> op = supplierRepo.findById(supplierId);
        if(op.isPresent()){
            return op.get();
        }
        return null;
    }

    public Page<Supplier> getSupplierList(Pageable pageable) {
        return supplierRepo.findAll(pageable);
    }

    public List<Supplier> getSupplierList() {

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
    
        return supplierRepo.findAll(sort);
    }





}
