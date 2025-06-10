package com.portifolio.apiintegration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.portifolio.apiintegration.dto.request.AddressRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void buscarEnderecoPorCep_DeveRetornarSucesso() throws Exception {
        mockMvc.perform(get("/api/v1/address/cep/01310100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cep").exists())
                .andExpect(jsonPath("$.logradouro").exists())
                .andExpect(jsonPath("$.cidade").exists())
                .andExpect(jsonPath("$.uf").exists());
    }

    @Test
    void buscarEnderecoPorCep_CepComTraco_DeveRetornarSucesso() throws Exception {
        mockMvc.perform(get("/api/v1/address/cep/01310-100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cep").exists());
    }

    @Test
    void buscarEnderecoPorCep_CepInvalido_DeveRetornarBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/address/cep/123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarEnderecoPost_DeveRetornarSucesso() throws Exception {
        AddressRequest request = new AddressRequest("01310100");

        mockMvc.perform(post("/api/v1/address/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cep").exists())
                .andExpect(jsonPath("$.logradouro").exists());
    }

    @Test
    void buscarEnderecoPost_CepVazio_DeveRetornarBadRequest() throws Exception {
        AddressRequest request = new AddressRequest("");

        mockMvc.perform(post("/api/v1/address/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarEnderecosLote_DeveRetornarSucesso() throws Exception {
        List<AddressRequest> requests = Arrays.asList(
                new AddressRequest("01310100"),
                new AddressRequest("20040020")
        );

        mockMvc.perform(post("/api/v1/address/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sucessos").exists())
                .andExpect(jsonPath("$.erros").exists())
                .andExpect(jsonPath("$.total").value(2))
                .andExpect(jsonPath("$.sucessos_count").exists())
                .andExpect(jsonPath("$.erros_count").exists());
    }

    @Test
    void healthCheck_DeveRetornarSucesso() throws Exception {
        mockMvc.perform(get("/api/v1/address/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Address Service"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }

    @Test
    void getMetrics_DeveRetornarSucesso() throws Exception {
        mockMvc.perform(get("/api/v1/address/metrics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memory_used_mb").exists())
                .andExpect(jsonPath("$.memory_total_mb").exists())
                .andExpect(jsonPath("$.processors").exists());
    }
}
