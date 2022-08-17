package com.example.projectemarketg3.controller;

import com.example.projectemarketg3.entity.Rating;
import com.example.projectemarketg3.exception.NotFoundException;
import com.example.projectemarketg3.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rating")
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;


    // get all rating rest api
    @GetMapping
    public List<Rating> getAllRating(){
        return ratingRepository.findAll();
    }

    // create a new rating rest api
    @PostMapping
    public Rating createRating(@RequestBody Rating rating) {
        return ratingRepository.save(rating);
    }

    // get rating by ID rest api
    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException
                        ("rating not exist with id :" + id));
        return ResponseEntity.ok(rating);
    }

    // update rating rest api
    @PutMapping("/{id}")
    public  ResponseEntity <Rating> updateRating(@PathVariable Long id, @RequestBody Boolean check){
        Rating rating = ratingRepository.findById(id)
                .orElseThrow (()->new NotFoundException
                        ("rating not exist with id :" + id));

        rating.setCreateAt(new Date(System.currentTimeMillis()));
        rating.setChecking(check);

        ratingRepository.save(rating);

        return  ResponseEntity.ok(ratingRepository.save(rating));
    }

    // delete rating rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteRating(@PathVariable Long id){
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException
                        ("rating not exist with id :" + id));
        ratingRepository.delete(rating);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

//    Get product star
    @GetMapping("/{star}/star")
    public List<Rating> getAllByStar(@PathVariable Integer star){
        return ratingRepository.getByStarOrderByCreateAtDesc(star);
    }
}
