package com.portifolio.apiintegration.controller;

import com.portifolio.apiintegration.client.viacep.ViaCepClient;
import com.portifolio.apiintegration.client.viacep.dto.ViaCepResponse;
import com.portifolio.apiintegration.dto.response.AddressResponse;
import com.portifolio.apiintegration.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private AddressService addressService;

    private ViaCepResponse viaCepResponse;

    @BeforeEach
    void setUp() {
        viaCepResponse = new ViaCepResponse();
        viaCepResponse.setCep("01310-100");
        viaCepResponse.setLogradouro("Avenida Paulista");
        viaCepResponse.setBairro("Bela Vista");
        viaCepResponse.setLocalidade("São Paulo");
        viaCepResponse.setUf("SP");
        viaCepResponse.setDdd("11");
    }

    @Test
    void buscarEnderecoPorCep_DeveRetornarEnderecoValido() {
        // Given
        String cep = "01310100";
        when(viaCepClient.consultarCep(cep)).thenReturn(viaCepResponse);

        // When
        AddressResponse result = addressService.buscarEnderecoPorCep(cep);

        // Then
        assertNotNull(result);
        assertEquals("01310-100", result.getCep());
        assertEquals("Avenida Paulista", result.getLogradouro());
        assertEquals("Bela Vista", result.getBairro());
        assertEquals("São Paulo", result.getCidade());
        assertEquals("SP", result.getUf());
        assertEquals("11", result.getDdd());

        verify(viaCepClient, times(1)).consultarCep(cep);
    }
}