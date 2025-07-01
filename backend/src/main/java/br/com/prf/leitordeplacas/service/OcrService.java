package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.Utils.MultipartInputStreamFileResource;
import br.com.prf.leitordeplacas.dto.OcrResponseDTO;
import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.ScanHistory;
import br.com.prf.leitordeplacas.repository.AppUserRepository;
import br.com.prf.leitordeplacas.repository.ScanHistoryRepository;
import br.com.prf.leitordeplacas.service.exception.AppUserNotFoundException;
import br.com.prf.leitordeplacas.service.exception.OcrProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Service
public class OcrService {

    private final RestTemplate restTemplate;
    private final ScanHistoryRepository scanHistoryRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public OcrService(RestTemplate restTemplate, ScanHistoryRepository scanHistoryRepository, AppUserRepository appUserRepository) {
        this.restTemplate = restTemplate;
        this.scanHistoryRepository = scanHistoryRepository;
        this.appUserRepository = appUserRepository;
    }

    public String sendFileToPythonApi(MultipartFile file) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String url = "http://localhost:5000/ocr/read"; // URL da Api Python !!!!!

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

    @Transactional
    public OcrResponseDTO processImageAndLogHistory(MultipartFile file, String location) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. BUSCA a entidade real e gerenciada no banco de dados. ESTA É A CORREÇÃO.
        AppUser currentUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new AppUserNotFoundException("Usuário do token não encontrado: " + username));

        String plate = this.sendFileToPythonApi(file);

        ScanHistory history = new ScanHistory();
        history.setScannedPlate(plate);
        history.setLocation(location);
        history.setScanTimestamp(Instant.now());
        history.setUser(currentUser);

        scanHistoryRepository.save(history);

        return new OcrResponseDTO(plate);
    }

}
