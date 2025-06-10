package com.portifolio.apiintegration.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dados de requisição para busca de endereço")
public class AddressRequest {

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP deve ter o formato 00000-000 ou 00000000")
    @Schema(description = "CEP para consulta", example = "01310-100", required = true)
    private String cep;

    @Schema(description = "Incluir informações adicionais na resposta", example = "true")
    private boolean includeAdditionalInfo = false;

    // Construtores
    public AddressRequest() {}

    public AddressRequest(String cep) {
        this.cep = cep;
    }

    public AddressRequest(String cep, boolean includeAdditionalInfo) {
        this.cep = cep;
        this.includeAdditionalInfo = includeAdditionalInfo;
    }

    // Getters e Setters
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public boolean isIncludeAdditionalInfo() {
        return includeAdditionalInfo;
    }

    public void setIncludeAdditionalInfo(boolean includeAdditionalInfo) {
        this.includeAdditionalInfo = includeAdditionalInfo;
    }

    @Override
    public String toString() {
        return "AddressRequest{" +
                "cep='" + cep + '\'' +
                ", includeAdditionalInfo=" + includeAdditionalInfo +
                '}';
    }
}
