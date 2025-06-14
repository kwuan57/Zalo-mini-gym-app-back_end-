package com.example.gym.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ZaloAuthService {
    String APP_ID = "your-app-id";
    String APP_SECRET = "your-app-secret";
    String TOKEN_URL = "https://oauth.zaloapp.com/v4/access_token";

    public String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // Tạo body cho yêu cầu POST
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("app_id", APP_ID);
        body.add("app_secret", APP_SECRET);
        body.add("grant_type", "authorization_code");

        // Tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Tạo request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu POST
        ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_URL, requestEntity, String.class);

        // Xử lý response
        return extractAccessToken(response.getBody());
    }

    private String extractAccessToken(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonResponse);

            // Kiểm tra xem access_token có tồn tại không
            if (node.has("access_token")) {
                return node.get("access_token").asText();
            } else {
                // Log lỗi chi tiết
                log.error("No access_token in response: {}", jsonResponse);
                throw new RuntimeException("No access_token found in Zalo response");
            }
        } catch (Exception e) {
            log.error("Failed to parse JSON response: {}", jsonResponse, e);
            throw new RuntimeException("Failed to parse access token: " + e.getMessage());
        }
    }
}
