package com.example.Project_eMarket_G3.controller;

import com.example.Project_eMarket_G3.entity.Ranking;
import com.example.Project_eMarket_G3.repository.RankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rank")
public class RankController {

    @Autowired
    private RankingRepository rankingRepository;

    @GetMapping
    public List<Ranking> getAllRank(){
        return rankingRepository.findAll();
    }
}