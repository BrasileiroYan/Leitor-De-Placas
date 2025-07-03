package br.com.prf.leitordeplacas.controller;

import br.com.prf.leitordeplacas.controller.exception.StandardError;
import br.com.prf.leitordeplacas.dto.AddressResponseDTO;
import br.com.prf.leitordeplacas.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/addresses")
@Tag(name = "Endereços", description = "Endpoint para listar todos os endereços dos proprietários dos veículos presentes no sistema")
public class AddressController {

    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Lista todos os endereços", description = "Retorna todos os endereços das pessoas disponíveis no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AddressResponseDTO.class)))),

            @ApiResponse(responseCode = "401", description = "Falha na autenticação (token ausente ou inválido)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não tem permissão)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> findAll() {
        List<AddressResponseDTO> addressList = addressService.findAll();

        return ResponseEntity.ok().body(addressList);
    }
}
