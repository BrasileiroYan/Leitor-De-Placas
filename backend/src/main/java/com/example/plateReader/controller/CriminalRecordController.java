package com.example.plateReader.controller;

import com.example.plateReader.controller.exception.StandardError;
import com.example.plateReader.dto.CriminalRecordResponseDTO;
import com.example.plateReader.service.CriminalRecordService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping(value = "/criminal-records")
@Tag(name = "Fichas criminais", description = "Endpoint para listar todas as fichas criminais presentes no sistema")
public class CriminalRecordController {

    private final CriminalRecordService criminalRecordService;

    public CriminalRecordController(CriminalRecordService criminalRecordService) {
        this.criminalRecordService = criminalRecordService;
    }

    @Operation(summary = "Lista todas as fichas criminais", description = "Retorna todas as fichas criminais disponíveis no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de crimes retornada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CriminalRecordResponseDTO.class)))),

            @ApiResponse(responseCode = "401", description = "Falha na autenticação (token ausente ou inválido)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não tem permissão)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping
    public ResponseEntity<List<CriminalRecordResponseDTO>> findAll() {
        List<CriminalRecordResponseDTO> criminalRecordListDTO = criminalRecordService.findAll();
        return ResponseEntity.ok().body(criminalRecordListDTO);
    }
}
