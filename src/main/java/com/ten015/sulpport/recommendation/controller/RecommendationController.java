package com.ten015.sulpport.recommendation.controller;

import com.ten015.sulpport.recommendation.dto.GreetingsRequest;
import com.ten015.sulpport.recommendation.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    //덕담
    @PostMapping ("greetings")
    public ResponseEntity<String> generateGreetings (@RequestBody GreetingsRequest greetingsRequest) {
        logger.info("덕담 컨트롤러 진입");
        String response = recommendationService.generateGreetings(greetingsRequest);
        return ResponseEntity.ok(response);
    }

    //용돈

}
