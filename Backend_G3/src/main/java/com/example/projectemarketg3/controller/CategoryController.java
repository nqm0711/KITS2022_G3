package com.example.projectemarketg3.controller;

import com.example.projectemarketg3.entity.Category;
import com.example.projectemarketg3.exception.NotFoundException;
import com.example.projectemarketg3.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    // create a new category rest api
    @PostMapping("/")
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    // get category by ID rest api
    @GetMapping("/{id}")
    public ResponseEntity<Category> getcategoryById(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException
                        ("category not exist with id :" + id));
        return ResponseEntity.ok(category);
    }

    // update category rest api
//    @PutMapping("/category/{id}")
//    public  ResponseEntity <Category> updatedCategory(@PathVariable Long id, @RequestBody Category categoryDetails){
//        Category category = categoryRepository.findById(id)
//                .orElseThrow (()->new NotFoundException
//                        ("category not exist with id :" + id));
//
//        category.set(categoryDetails.get());
//        category.set(categoryDetails.get());
//        category.set(categoryDetails.get());
//
//        Category updatedCategory = categoryRepository.save(category);
//
//        return  ResponseEntity.ok(updatedCategory);
//    }

    // delete category rest api
    @DeleteMapping("/{id}")
    public ResponseEntity <Map<String, Boolean>> deletecategory(@PathVariable Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException
                        ("category not exist with id :" + id));
        categoryRepository.delete(category);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
