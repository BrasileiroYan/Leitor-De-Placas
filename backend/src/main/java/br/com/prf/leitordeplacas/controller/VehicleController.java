package br.com.prf.leitordeplacas.controller;

import br.com.prf.leitordeplacas.controller.exception.StandardError;
import br.com.prf.leitordeplacas.dto.CrimeResponseDTO;
import br.com.prf.leitordeplacas.dto.CriminalRecordResponseDTO;
import br.com.prf.leitordeplacas.dto.PersonResponseDTO;
import br.com.prf.leitordeplacas.dto.VehicleResponseDTO;
import br.com.prf.leitordeplacas.service.VehicleReportPdfService;
import br.com.prf.leitordeplacas.service.VehicleService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/vehicles")
@Tag(name = "Veículos", description = "Endpoint para busca de veículos e informações associadas à eles (proprietários, fichas criminais e crimes")
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleReportPdfService vehicleReportPdfService;

    public VehicleController(VehicleService vehicleService, VehicleReportPdfService vehicleReportPdfService) {
        this.vehicleService = vehicleService;
        this.vehicleReportPdfService = vehicleReportPdfService;
    }

    @Operation(summary = "Lista todos os veículos", description = "Retorna todos os veículos disponíveis no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de proprietários retornada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = VehicleResponseDTO.class)))),

            @ApiResponse(responseCode = "401", description = "Falha na autenticação (token ausente ou inválido)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),

            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não tem permissão)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> findAll() {
        List<VehicleResponseDTO> vehiclesDTO = vehicleService.findAll();

        return ResponseEntity.ok().body(vehiclesDTO);
    }

    @Operation(summary = "Busca a partir da placa", description = "Realiza a busca das informações de um veículo a partir da placa fornecida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca pelo veículo bem-sucedida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehicleResponseDTO.class))),

            @ApiResponse(responseCode = "404", description = "Veículo com a placa fornecida não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
    })
    @GetMapping(path = "/{plate}")
    public ResponseEntity<VehicleResponseDTO> findByPlate(@PathVariable String plate) {
        VehicleResponseDTO vehicleDTO = vehicleService.findByPlate(plate);

        return ResponseEntity.ok().body(vehicleDTO);
    }

    @Operation(summary = "Gera e baixa PDF", description = "Gera um documento PDF com as informações de um veículo e faz o download para o dispositivo do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF baixado com sucesso",
                    content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))),

            @ApiResponse(responseCode = "500", description = "Erro na geração do PDF",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
    })
    @GetMapping(path = "/{plate}/download-pdf")
    public ResponseEntity<byte[]> generateVehiclePdfReport(@PathVariable String plate) {
        byte[] pdfBytes = vehicleReportPdfService.generateVehicleReportPDF(plate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio_placa_" + plate + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @Operation(summary = "Busca proprietário a partir da placa", description = "Realiza a busca pelo dono de um veículo a partir da placa fornecida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca pelo proprietário bem-sucedida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponseDTO.class))),

            @ApiResponse(responseCode = "404", description = "Proprietário do veículo com a placa fornecida não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
    })
    @GetMapping(path = "/{plate}/owner")
    public ResponseEntity<PersonResponseDTO> findOwnerByPlate(@PathVariable String plate) {
        PersonResponseDTO ownerDTO = vehicleService.findOwnerByPlate(plate);

        return ResponseEntity.ok().body(ownerDTO);
    }

    @Operation(summary = "Busca ficha criminal a partir da placa", description = "Realiza a busca da ficha criminal associada à um veículo a partir da placa fornecida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca pelo ficha criminal bem-sucedida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriminalRecordResponseDTO.class))),

            @ApiResponse(responseCode = "404", description = "Ficha criminal associada ao veículo com a placa fornecida não encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
    })
    @GetMapping(path = "/{plate}/owner/criminal-record")
    public ResponseEntity<CriminalRecordResponseDTO> findCriminalRecordByPlate(@PathVariable String plate) {
        CriminalRecordResponseDTO criminalRecordDTO = vehicleService.findCriminalRecordByPlate(plate);

        return ResponseEntity.ok().body(criminalRecordDTO);
    }

    @Operation(summary = "Busca todo os crimes a partir da placa", description = "Realiza a busca por todos os crimes na ficha criminal do proprietário de um veículo a partir da placa fornecida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca pelos crimes bem-sucedida",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CrimeResponseDTO.class)))),

            @ApiResponse(responseCode = "404", description = "Crimes associados ao veículo com a placa fornecida não encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
    })
    @GetMapping(path = "/{plate}/owner/criminal-record/crimes")
    public ResponseEntity<List<CrimeResponseDTO>> findAllCrimesByPlate(@PathVariable String plate) {
        List<CrimeResponseDTO> crimesDTO = vehicleService.findAllCrimesByPlate(plate);

        return ResponseEntity.ok().body(crimesDTO);
    }

    @Operation(summary = "Busca um crime a partir da placa e ID", description = "Realiza a busca de um crime associado à um veículo a partir da placa e do ID do crime fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca pelo crime bem-sucedida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CrimeResponseDTO.class))),

            @ApiResponse(responseCode = "404", description = "Crime com tal ID e com a placa do veículo fornecidos não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
    })
    @GetMapping(path = "/{plate}/owner/criminal-record/crime/{id}")
    public ResponseEntity<CrimeResponseDTO> findCrimeByPlateAndId(@PathVariable String plate, @PathVariable Long id) {
        CrimeResponseDTO crimeDTO = vehicleService.findCrimeByPlateAndId(plate, id);

        return ResponseEntity.ok().body(crimeDTO);
    }
}
