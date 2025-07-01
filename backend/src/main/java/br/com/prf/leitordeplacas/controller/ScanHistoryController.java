package br.com.prf.leitordeplacas.controller;

import br.com.prf.leitordeplacas.controller.exception.StandardError;
import br.com.prf.leitordeplacas.dto.ScanHistoryResponseDTO;
import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.service.ScanHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
@Tag(name = "Histórico", description = "Endpoint para a função de histórico das placas lidas")
public class ScanHistoryController {

    private final ScanHistoryService scanHistoryService;

    public ScanHistoryController(ScanHistoryService scanHistoryService) {
        this.scanHistoryService = scanHistoryService;
    }

    @Operation(summary = "Exibe o histórico das placas", description = "Retorna uma lista com as placas de veículo mais recentes escaneadas pelo usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScanHistoryResponseDTO.class)))),

            @ApiResponse(responseCode = "401", description = "Falha na autenticação (token ausente ou inválido)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não tem permissão)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping("/scans")
    public ResponseEntity<Page<ScanHistoryResponseDTO>> getMyRecentScans(
            @AuthenticationPrincipal AppUser currentUser,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Page<ScanHistoryResponseDTO> historyPage = scanHistoryService.getRecentScansForUser(currentUser, page, size);
        return ResponseEntity.ok(historyPage);
    }
}
