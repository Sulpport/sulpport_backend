package com.ten015.sulpport.recommendation.service;

import com.ten015.sulpport.recommendation.dto.GreetingsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RecommendationServiceImpl(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    @Override
    public String generatePrompt(GreetingsRequest greetingsReauest) {
        logger.info("service진입");
        Map<String, Object> requestBody = new HashMap<>();

        // 모델 지정
        requestBody.put("model", "gpt-3.5-turbo");
        // max_tokens 지정
        requestBody.put("max_tokens", 500);

        // 대화 메시지 구성
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "사용자 메시지");
        messages.add(message);

        // 받는 사람 비어있을 경우
        String name = Optional.ofNullable(greetingsReauest.getName()).orElse("이름");

        // prompt
        String userMessage = String.format("%s의 나이는 %s이며, 나와의 관계는 %s입니다. 상대방의 현재 상황은 '%s'이며, 이에 맞는 설날 덕담을 %s로, 답변은 %s 어조를 사용해줘",
                name,
                greetingsReauest.getAgeGroup(),
                greetingsReauest.getRelations(),
                greetingsReauest.getSituation(),
                greetingsReauest.getFormality(),
                String.join(", ", greetingsReauest.getTone()));
        messages.add(Map.of("role", "user", "content", userMessage));

        // body에 prompt 담기
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, httpHeaders);
        logger.info("service진입 - request : {}", request);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        logger.info("service진입 - response : {}", response);

        return response.getBody();
    }

}
