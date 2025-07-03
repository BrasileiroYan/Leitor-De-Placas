package br.com.prf.leitordeplacas.controller;

import br.com.prf.leitordeplacas.controller.exception.StandardError;
import br.com.prf.leitordeplacas.dto.CrimeResponseDTO;
import br.com.prf.leitordeplacas.service.CrimeService;
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
@RequestMapping(value = "/crimes")
@Tag(name = "Crimes", description = "Endpoint para listar todos os crimes presentes no sistema")
public class CrimeController {

    private final CrimeService crimeService;

    public CrimeController(CrimeService crimeRecordService) {
        this.crimeService = crimeRecordService;
    }

    @Operation(summary = "Lista todos os crimes", description = "Retorna todos os crimes disponíveis no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de crimes retornada com sucesso",
                content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CrimeResponseDTO.class)))),

            @ApiResponse(responseCode = "401", description = "Falha na autenticação (token ausente ou inválido)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não tem permissão)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping
    public ResponseEntity<List<CrimeResponseDTO>> findAll(){
        List<CrimeResponseDTO> crimesDTO = crimeService.findAll();

        return ResponseEntity.ok().body(crimesDTO);
    }
}
