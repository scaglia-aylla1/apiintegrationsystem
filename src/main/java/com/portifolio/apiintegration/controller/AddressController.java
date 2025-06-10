package com.portifolio.apiintegration.controller;

import com.portifolio.apiintegration.dto.request.AddressRequest;
import com.portifolio.apiintegration.dto.response.AddressResponse;
import com.portifolio.apiintegration.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/address")
@Tag(name = "Address", description = "API para consulta de endereços")
@Validated
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

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
            @PathVariable
            @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP deve ter o formato 00000-000 ou 00000000")
            String cep) {

        logger.info("Recebida requisição para buscar CEP: {}", cep);
        AddressResponse address = addressService.buscarEnderecoPorCep(cep);
        logger.info("CEP {} processado com sucesso", cep);
        return ResponseEntity.ok(address);
    }

    @PostMapping("/search")
    @Operation(summary = "Buscar endereço via POST",
            description = "Busca endereço usando dados estruturados na requisição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos"),
            @ApiResponse(responseCode = "502", description = "Erro na integração com ViaCEP"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<AddressResponse> buscarEnderecoPost(@Valid @RequestBody AddressRequest request) {
        logger.info("Recebida requisição POST para buscar endereço: {}", request);
        AddressResponse address = addressService.buscarEnderecoPorCep(request.getCep());
        logger.info("Requisição POST processada com sucesso para CEP: {}", request.getCep());
        return ResponseEntity.ok(address);
    }

    @PostMapping("/batch")
    @Operation(summary = "Buscar múltiplos endereços",
            description = "Busca vários endereços em uma única requisição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereços processados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Lista de CEPs inválida"),
            @ApiResponse(responseCode = "502", description = "Erro na integração com ViaCEP"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Map<String, Object>> buscarEnderecosLote(@Valid @RequestBody List<AddressRequest> requests) {
        logger.info("Recebida requisição em lote para {} CEPs", requests.size());

        Map<String, Object> response = new HashMap<>();
        Map<String, AddressResponse> enderecos = new HashMap<>();
        Map<String, String> erros = new HashMap<>();

        for (AddressRequest request : requests) {
            try {
                AddressResponse address = addressService.buscarEnderecoPorCep(request.getCep());
                enderecos.put(request.getCep(), address);
            } catch (Exception e) {
                logger.warn("Erro ao processar CEP {}: {}", request.getCep(), e.getMessage());
                erros.put(request.getCep(), e.getMessage());
            }
        }

        response.put("sucessos", enderecos);
        response.put("erros", erros);
        response.put("total", requests.size());
        response.put("sucessos_count", enderecos.size());
        response.put("erros_count", erros.size());

        logger.info("Processamento em lote concluído: {} sucessos, {} erros",
                enderecos.size(), erros.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica se o serviço está funcionando")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Address Service");
        health.put("timestamp", System.currentTimeMillis());
        health.put("version", "1.0.0");

        return ResponseEntity.ok(health);
    }

    @GetMapping("/metrics")
    @Operation(summary = "Métricas do serviço", description = "Retorna métricas básicas do serviço")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // Informações básicas da JVM
        Runtime runtime = Runtime.getRuntime();
        metrics.put("memory_used_mb", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
        metrics.put("memory_total_mb", runtime.totalMemory() / 1024 / 1024);
        metrics.put("memory_max_mb", runtime.maxMemory() / 1024 / 1024);
        metrics.put("processors", runtime.availableProcessors());
        metrics.put("uptime_ms", System.currentTimeMillis());

        return ResponseEntity.ok(metrics);
    }
}