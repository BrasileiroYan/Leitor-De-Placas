package br.com.prf.leitordeplacas.controller;

import br.com.prf.leitordeplacas.controller.exception.StandardError;
import br.com.prf.leitordeplacas.dto.OcrResponseDTO;
import br.com.prf.leitordeplacas.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/ocr")
@Tag(name = "OCR", description = "Serviços de Reconhecimento Óptico de Caracteres (OCR) para leitura de placas de veículos.")
public class OcrController {

    private final OcrService ocrService;

    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @Operation(summary = "Lê a placa de um veículo a partir de uma imagem",
            description = "Recebe uma imagem (foto) de um veículo e utiliza um serviço de OCR para extrair e retornar o texto da placa. Suporta apenas arquivos de imagem.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Placa lida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OcrResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: arquivo não fornecido ou formato incorreto)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ou no serviço OCR",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping(value = "/read", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OcrResponseDTO> readPlateFromImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("location") String location
    ) {
        OcrResponseDTO response = ocrService.processImageAndLogHistory(file, location);

        return ResponseEntity.ok(response);
    }
}
