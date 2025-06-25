package com.example.plateReader.controller;

import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.enums.Role;
import com.example.plateReader.repository.AppUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.example.plateReader.model.ScanHistory;
import com.example.plateReader.repository.ScanHistoryRepository;
import org.springframework.test.annotation.DirtiesContext;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class OcrControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate; // Spring injeta o mesmo RestTemplate usado pelo OcrService

    @Autowired
    private ObjectMapper objectMapper; // Utilitário para converter objetos em JSON

    @Autowired
    private ScanHistoryRepository scanHistoryRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockRestServiceServer mockServer;

    private static final String TEST_USERNAME = "admin.test@prf.gov.br";
    private AppUser testAdminUser;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);

        testAdminUser = new AppUser();
        testAdminUser.setUsername(TEST_USERNAME);
        testAdminUser.setPassword(passwordEncoder.encode("@Admin_PRF123"));
        testAdminUser.setRole(Role.ADMIN);
        testAdminUser.setEnabled(true);
        appUserRepository.save(testAdminUser);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME, roles = "ADMIN")
    @Transactional
    void readPlateFromImage_whenApiSucceeds_shouldReturnOkAndPlate() throws Exception {
        // --- ARRANGE (Preparação) ---

        // 1. Define a URL da API Python que esperamos que seja chamada
        String pythonApiUrl = "http://localhost:5000/ocr/read";
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
        String location = "BR-116, Km 50";

        // --- ACT & ASSERT (Ação e Verificação) ---

        // 5. Simula a chamada do front-end para o nosso OcrController
        mockMvc.perform(multipart("/ocr/read").file(mockImage).param("location", location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate", is(expectedPlate))); // Verifica se o JSON de resposta tem o campo "plate" com o valor correto

        // 6. Garante que todas as expectativas do mockServer foram cumpridas
        mockServer.verify();
        var historyEntries = scanHistoryRepository.findAll();
        assertThat(historyEntries).hasSize(1);

        ScanHistory savedEntry = historyEntries.getFirst();
        assertThat(savedEntry.getScannedPlate()).isEqualTo(expectedPlate);
        assertThat(savedEntry.getLocation()).isEqualTo(location);
        assertThat(savedEntry.getUser()).isNotNull();
        assertThat(savedEntry.getUser().getId()).isEqualTo(testAdminUser.getId());
        assertThat(savedEntry.getUser().getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME, roles = "ADMIN")
    void readPlateFromImage_whenApiFails_shouldReturnInternalServerError() throws Exception {
        // --- ARRANGE ---
        String pythonApiUrl = "http://localhost:5000/ocr/read";

        // Configura o servidor mock para retornar um erro 500
        mockServer.expect(requestTo(pythonApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError()); // Responde com HTTP 500

        MockMultipartFile mockImage = new MockMultipartFile("file", "placa-erro.jpg", MediaType.IMAGE_JPEG_VALUE, "error-image".getBytes());

        // --- ACT & ASSERT ---
        mockMvc.perform(multipart("/ocr/read").file(mockImage).param("location", "Local de Teste"))
                .andExpect(status().isInternalServerError()); // Esperamos que nosso controller trate o erro e retorne 500

        mockServer.verify();
    }
}
