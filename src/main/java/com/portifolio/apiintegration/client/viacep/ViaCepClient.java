package com.portifolio.apiintegration.client.viacep;

import com.portifolio.apiintegration.client.viacep.dto.ViaCepResponse;
import com.portifolio.apiintegration.exception.ApiIntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ViaCepClient {

    private static final Logger logger = LoggerFactory.getLogger(ViaCepClient.class);
    private static final String VIACEP_BASE_URL = "https://viacep.com.br/ws";

    @Autowired
    private RestTemplate restTemplate;

    public ViaCepResponse consultarCep(String cep) {
        try {
            logger.info("Consultando CEP: {}", cep);

            String cepLimpo = limparCep(cep);
            validarCep(cepLimpo);

            String url = String.format("%s/%s/json/", VIACEP_BASE_URL, cepLimpo);
            logger.debug("URL da requisição: {}", url);

            ViaCepResponse response = restTemplate.getForObject(url, ViaCepResponse.class);

            if (response == null) {
                throw new ApiIntegrationException("ViaCEP", "consultarCep",
                        "Resposta nula da API");
            }

            if (Boolean.TRUE.equals(response.getErro())) {
                throw new ApiIntegrationException("ViaCEP", "consultarCep",
                        "CEP não encontrado: " + cep);
            }

            logger.info("CEP {} consultado com sucesso", cep);
            return response;

        } catch (RestClientException e) {
            logger.error("Erro ao consultar CEP {}: {}", cep, e.getMessage());
            throw new ApiIntegrationException("ViaCEP", "consultarCep",
                    "Erro na comunicação com a API", e);
        }
    }

    private String limparCep(String cep) {
        if (cep == null) {
            throw new IllegalArgumentException("CEP não pode ser nulo");
        }
        return cep.replaceAll("[^0-9]", "");
    }

    private void validarCep(String cep) {
        if (cep.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter exatamente 8 dígitos");
        }

        if (!cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP deve conter apenas números");
        }
    }
}