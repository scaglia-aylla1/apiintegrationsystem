package com.portifolio.apiintegration.service;

import com.portifolio.apiintegration.client.viacep.ViaCepClient;
import com.portifolio.apiintegration.client.viacep.dto.ViaCepResponse;
import com.portifolio.apiintegration.dto.response.AddressResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    private ViaCepClient viaCepClient;

    @Cacheable(value = "addresses", key = "#cep")
    public AddressResponse buscarEnderecoPorCep(String cep) {
        logger.info("Buscando endereço para CEP: {}", cep);

        ViaCepResponse viaCepResponse = viaCepClient.consultarCep(cep);

        AddressResponse response = new AddressResponse(
                viaCepResponse.getCep(),
                viaCepResponse.getLogradouro(),
                viaCepResponse.getComplemento(),
                viaCepResponse.getBairro(),
                viaCepResponse.getLocalidade(),
                viaCepResponse.getLocalidade(), // Nome completo da cidade
                viaCepResponse.getUf(),
                viaCepResponse.getDdd()
        );

        logger.info("Endereço encontrado para CEP {}: {}, {}-{}",
                cep, response.getLogradouro(), response.getCidade(), response.getUf());

        return response;
    }
}
