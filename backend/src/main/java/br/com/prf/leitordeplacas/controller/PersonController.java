package br.com.prf.leitordeplacas.controller;

import br.com.prf.leitordeplacas.controller.exception.StandardError;
import br.com.prf.leitordeplacas.dto.PersonResponseDTO;
import br.com.prf.leitordeplacas.service.PersonService;
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
@RequestMapping(value = "/owners")
@Tag(name = "Proprietários", description = "Endpoint para listar todos os proprietários dos veículos presentes no sistema")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personRecordService) {
        this.personService = personRecordService;
    }

    @Operation(summary = "Lista todos os proprietários", description = "Retorna todos os proprietários de veículos disponíveis no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de proprietários retornada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonResponseDTO.class)))),

            @ApiResponse(responseCode = "401", description = "Falha na autenticação (token ausente ou inválido)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não tem permissão)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> findAll() {
        List<PersonResponseDTO> peopleDTO = personService.findAll();
        return ResponseEntity.ok().body(peopleDTO);
    }
}
