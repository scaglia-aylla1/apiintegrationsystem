package com.portifolio.apiintegration.exception;

public class ApiIntegrationException extends RuntimeException{

    private final String apiName;
    private final String operation;

    public ApiIntegrationException(String apiName, String operation, String message) {
        super(String.format("[%s] Erro na operação '%s': %s", apiName, operation, message));
        this.apiName = apiName;
        this.operation = operation;
    }

    public ApiIntegrationException(String apiName, String operation, String message, Throwable cause) {
        super(String.format("[%s] Erro na operação '%s': %s", apiName, operation, message), cause);
        this.apiName = apiName;
        this.operation = operation;
    }

    public String getApiName() {
        return apiName;
    }

    public String getOperation() {
        return operation;
    }
}
