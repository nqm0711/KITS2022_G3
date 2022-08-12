package com.example.Project_eMarket_G3.controller;

import com.example.Project_eMarket_G3.entity.Category;
import com.example.Project_eMarket_G3.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
}
