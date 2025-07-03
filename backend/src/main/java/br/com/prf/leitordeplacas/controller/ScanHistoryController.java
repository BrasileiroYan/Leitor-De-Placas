package br.com.prf.leitordeplacas.controller;

import br.com.prf.leitordeplacas.controller.exception.StandardError;
import br.com.prf.leitordeplacas.dto.PlateScanRequestDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scans")
@Tag(name = "Histórico", description = "Endpoint para a função de histórico das placas lidas")
public class ScanHistoryController {

    private final ScanHistoryService scanHistoryService;

    public ScanHistoryController(ScanHistoryService scanHistoryService) {
        this.scanHistoryService = scanHistoryService;
    }

    @Operation(summary = "Registra uma nova placa no histórico do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Placa salva no histórico com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScanHistoryResponseDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Erro inesperado ao tentar registrar a placa lida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<Void> registerScan(
            @RequestBody PlateScanRequestDTO request,
            @AuthenticationPrincipal AppUser currentUser) {

        scanHistoryService.registerNewScan(request.getPlate(), currentUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
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
    @GetMapping("/history")
    public ResponseEntity<Page<ScanHistoryResponseDTO>> getMyRecentScans(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Page<ScanHistoryResponseDTO> historyPage = scanHistoryService.getRecentScansForUser(page, size);
        return ResponseEntity.ok(historyPage);
    }
}
