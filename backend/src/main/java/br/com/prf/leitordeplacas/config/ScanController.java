package br.com.prf.leitordeplacas.config;

import br.com.prf.leitordeplacas.dto.PlateScanRequestDTO;
import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.service.ScanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scans")
@Tag(name = "Registro de Scans", description = "Endpoint para registrar placas escaneadas.")
public class ScanController {
    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @Operation(summary = "Registra uma nova placa escaneada no histórico do usuário")
    @PostMapping("/register")
    public ResponseEntity<Void> registerScan(
            @RequestBody PlateScanRequestDTO request,
            @AuthenticationPrincipal AppUser currentUser) {

        scanService.registerNewScan(request.getPlate(), currentUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
