package com.example.Project_eMarket_G3.controller;

import com.example.Project_eMarket_G3.entity.Supplier;
import com.example.Project_eMarket_G3.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public List<Supplier> getAllSup(){
        return supplierRepository.findAll();
    }


}
