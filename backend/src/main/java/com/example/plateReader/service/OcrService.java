package com.example.plateReader.service;

import com.example.plateReader.Utils.MultipartInputStreamFileResource;
import com.example.plateReader.service.exception.OcrProcessingException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Service
public class OcrService {

    private final RestTemplate restTemplate;

    public OcrService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String sendFileToPythonApi(MultipartFile file) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String url = "http://localhost:5000/ocr"; // URL da Api Python !!!!!

            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("plate");
            } else {
                throw new OcrProcessingException("Resposta inválida da API Python");
            }
        } catch (IOException e) {
            throw new OcrProcessingException("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}
