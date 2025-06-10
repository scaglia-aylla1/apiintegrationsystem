# 🏠 Integração ViaCEP - Consulta de Endereços

## 📋 Visão Geral

Esta integração permite consultar informações de endereços brasileiros através do CEP, utilizando a API pública do ViaCEP.

## 🚀 Funcionalidades

- ✅ Consulta individual de CEP
- ✅ Consulta em lote (múltiplos CEPs)
- ✅ Cache inteligente (5 minutos)
- ✅ Validação de entrada robusta
- ✅ Tratamento de erros personalizado
- ✅ Logs detalhados
- ✅ Métricas de sistema
- ✅ Health check

## 🛠️ Tecnologias Utilizadas

- **Spring Boot** - Framework principal
- **RestTemplate** - Cliente HTTP
- **Caffeine Cache** - Sistema de cache
- **Bean Validation** - Validação de dados
- **Swagger/OpenAPI** - Documentação da API
- **JUnit 5** - Testes unitários
- **Mockito** - Mock para testes

## 📡 Endpoints Disponíveis

### 1. Consulta Individual (GET)
```http
GET /api/v1/address/cep/{cep}
```

**Parâmetros:**
- `cep` (path): CEP no formato 00000000 ou 00000-000

**Exemplo:**
```bash
curl -X GET "http://localhost:8080/api/v1/address/cep/01310100"
```

**Resposta:**
```json
{
  "cep": "01310-100",
  "logradouro": "Avenida Paulista",
  "complemento": "lado ímpar",
  "bairro": "Bela Vista",
  "cidade": "São Paulo",
  "estado": "São Paulo",
  "uf": "SP",
  "ddd": "11"
}
```

### 2. Consulta via POST
```http
POST /api/v1/address/search
Content-Type: application/json

{
  "cep": "01310100",
  "includeAdditionalInfo": false
}
```

### 3. Consulta em Lote
```http
POST /api/v1/address/batch
Content-Type: application/json

[
  {"cep": "01310100"},
  {"cep": "20040020"},
  {"cep": "30112000"}
]
```

**Resposta:**
```json
{
  "sucessos": {
    "01310100": {
      "cep": "01310-100",
      "logradouro": "Avenida Paulista",
      "bairro": "Bela Vista",
      "cidade": "São Paulo",
      "uf": "SP"
    }
  },
  "erros": {
    "00000000": "CEP não encontrado"
  },
  "total": 2,
  "sucessos_count": 1,
  "erros_count": 1
}
```

### 4. Health Check
```http
GET /api/v1/address/health
```

### 5. Métricas do Sistema
```http
GET /api/v1/address/metrics
```

## ⚙️ Configurações

### Cache
```yaml
spring:
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=1000,expireAfterWrite=300s
```

### Timeouts
- **Connection Timeout**: 5 segundos
- **Read Timeout**: 10 segundos
- **Cache TTL**: 5 minutos

### Logs
```yaml
logging:
  level:
    com.portfolio.apiintegration: DEBUG
    org.springframework.web.client.RestTemplate: DEBUG
```

## 🧪 Como Testar

### 1. Testes Automatizados
```bash
./mvnw test
```

### 2. Testes Manuais com curl

**CEP válido:**
```bash
curl -X GET "http://localhost:8080/api/v1/address/cep/01310100"
```

**CEP com traço:**
```bash
curl -X GET "http://localhost:8080/api/v1/address/cep/01310-100"
```

**CEP inválido:**
```bash
curl -X GET "http://localhost:8080/api/v1/address/cep/123"
```

**Consulta POST:**
```bash
curl -X POST "http://localhost:8080/api/v1/address/search" \
  -H "Content-Type: application/json" \
  -d '{"cep": "01310100"}'
```

**Consulta em lote:**
```bash
curl -X POST "http://localhost:8080/api/v1/address/batch" \
  -H "Content-Type: application/json" \
  -d '[{"cep": "01310100"}, {"cep": "20040020"}]'
```

### 3. Swagger UI
Acesse: `http://localhost:8080/swagger-ui.html`

## 🔧 Tratamento de Erros

### Códigos de Status HTTP

| Código | Descrição | Causa |
|--------|-----------|-------|
| 200 | OK | Sucesso |
| 400 | Bad Request | CEP inválido ou dados malformados |
| 502 | Bad Gateway | Erro na API do ViaCEP |
| 500 | Internal Server Error | Erro interno do servidor |

### Exemplos de Erros

**CEP inválido:**
```json
{
  "timestamp": "2025-06-09T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "CEP deve ter o formato 00000-000 ou 00000000"
}
```

**CEP não encontrado:**
```json
{
  "timestamp": "2025-06-09T10:30:00",
  "status": 502,
  "error": "API Integration Error",
  "message": "[ViaCEP] Erro na operação 'consultarCep': CEP não encontrado: 00000000",
  "api": "ViaCEP",
  "operation": "consultarCep"
}
```

## 📊 Monitoramento

### Logs Importantes
- Requisições recebidas
- CEPs consultados com sucesso
- Erros de integração
- Performance de cache

### Métricas Disponíveis
- Uso de memória JVM
- Número de processadores
- Tempo de execução
- Status do serviço

## 🔍 Validações Implementadas

### CEP
- ✅ Não pode ser nulo ou vazio
- ✅ Deve ter exatamente 8 dígitos
- ✅ Aceita formato com ou sem traço
- ✅ Remove caracteres não numéricos automaticamente

### Lote
- ✅ Lista não pode ser vazia
- ✅ Cada item da lista é validado individualmente
- ✅ Erros não interrompem o processamento dos demais

## 🚀 Performance

### Cache
- **Estratégia**: Write-Through
- **TTL**: 5 minutos
- **Capacidade**: 1000 entradas
- **Eviction**: LRU (Least Recently Used)

### Otimizações
- Connection pooling no RestTemplate
- Timeouts configurados adequadamente
- Logs estruturados para troubleshooting
- Validação early para reduzir chamadas desnecessárias

## 📝 Próximos Passos

1. ✅ **Implementação básica** - Concluída
2. ✅ **Validações e tratamento de erro** - Concluída
3. ✅ **Cache e performance** - Concluída
4. ✅ **Testes unitários e integração** - Concluída
5. 🔄 **Integração com banco de dados** - Próximo
6. 🔄 **Rate limiting** - Planejado
7. 🔄 **Monitoramento avançado** - Planejado

## 📚 Referências

- [ViaCEP API Documentation](https://viacep.com.br/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Caffeine Cache](https://github.com/ben-manes/caffeine)
- [Bean Validation](https://beanvalidation.org/)

---

> 💡 **Dica**: Use o cache de forma inteligente! CEPs raramente mudam, então o cache de 5 minutos oferece excelente performance sem comprometer a precisão dos dados.

## 🧑‍💻 Desenvolvido por:
**Aylla Scaglia** -
[GitHub](https://github.com/scaglia-aylla1) -

📌 Este é um projeto pessoal, e tem o objetivo de aplicar os conhecimentos adquiridos sobre Spring Boot, JPA e boas práticas de desenvolvimento backend.
