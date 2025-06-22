package com.example.plateReader.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OcrControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate; // Spring injeta o mesmo RestTemplate usado pelo OcrService

    @Autowired
    private ObjectMapper objectMapper; // Utilitário para converter objetos em JSON

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        // Cria o servidor mock que vai interceptar as chamadas do RestTemplate
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void readPlateFromImage_whenApiSucceeds_shouldReturnOkAndPlate() throws Exception {
        // --- ARRANGE (Preparação) ---

        // 1. Define a URL da API Python que esperamos que seja chamada
        String pythonApiUrl = "http://localhost:5000/ocr";
        String expectedPlate = "ABC1234";

        // 2. Cria a resposta JSON que a API Python FAKE irá retornar
        Map<String, String> fakeApiResponse = Map.of("plate", expectedPlate);
        String fakeApiResponseBody = objectMapper.writeValueAsString(fakeApiResponse);

        // 3. Configura o servidor mock:
        // "ESPERE uma requisição para a URL 'pythonApiUrl' com o método POST,
        // e quando isso acontecer, RESPONDA com SUCESSO (HTTP 200),
        // com o corpo 'fakeApiResponseBody' e o Content-Type 'application/json'."
        mockServer.expect(requestTo(pythonApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(fakeApiResponseBody, MediaType.APPLICATION_JSON));

        // 4. Cria um arquivo de imagem falso para o upload
        MockMultipartFile mockImage = new MockMultipartFile(
                "file",                 // Nome do @RequestParam no controller
                "placa-teste.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-bytes".getBytes()
        );

        // --- ACT & ASSERT (Ação e Verificação) ---

        // 5. Simula a chamada do front-end para o nosso OcrController
        mockMvc.perform(multipart("/ocr/read").file(mockImage))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate", is(expectedPlate))); // Verifica se o JSON de resposta tem o campo "plate" com o valor correto

        // 6. Garante que todas as expectativas do mockServer foram cumpridas
        mockServer.verify();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void readPlateFromImage_whenApiFails_shouldReturnInternalServerError() throws Exception {
        // --- ARRANGE ---
        String pythonApiUrl = "http://localhost:5000/ocr";

        // Configura o servidor mock para retornar um erro 500
        mockServer.expect(requestTo(pythonApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError()); // Responde com HTTP 500

        MockMultipartFile mockImage = new MockMultipartFile("file", "placa-erro.jpg", MediaType.IMAGE_JPEG_VALUE, "error-image".getBytes());

        // --- ACT & ASSERT ---
        mockMvc.perform(multipart("/ocr/read").file(mockImage))
                .andExpect(status().isInternalServerError()); // Esperamos que nosso controller trate o erro e retorne 500

        mockServer.verify();
    }
}
