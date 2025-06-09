package com.portifolio.apiintegration.controller;

import com.portifolio.apiintegration.dto.response.AddressResponse;
import com.portifolio.apiintegration.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
@Tag(name = "Address", description = "API para consulta de endereços")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/cep/{cep}")
    @Operation(summary = "Buscar endereço por CEP",
            description = "Consulta informações de endereço através do CEP utilizando a API ViaCEP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "CEP inválido"),
            @ApiResponse(responseCode = "502", description = "Erro na integração com ViaCEP"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<AddressResponse> buscarEnderecoPorCep(
            @Parameter(description = "CEP para consulta (8 dígitos)", example = "01310-100")
            @PathVariable String cep) {

        AddressResponse address = addressService.buscarEnderecoPorCep(cep);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica se o serviço está funcionando")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Address Service is running!");
    }
}
