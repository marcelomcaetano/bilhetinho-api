package br.com.mmc.bilhetinho_api.service;

import br.com.mmc.bilhetinho_api.dto.EnderecoResponseDTO;
import br.com.mmc.bilhetinho_api.exception.BusinessRuleException;
import br.com.mmc.bilhetinho_api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ViaCepServiceTest {

    private MockRestServiceServer mockServer;
    private ViaCepService viaCepService;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://viacep.com.br/ws");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        viaCepService = new ViaCepService(builder.build());
    }

    @Test
    @DisplayName("Deve buscar endereço por CEP válido com sucesso")
    void deveBuscarEnderecoPorCepComSucesso() {
        String json = """
            {
                "cep": "38060-480",
                "logradouro": "Rua Novo Horizonte",
                "complemento": "",
                "bairro": "Vila Santa Maria",
                "localidade": "Uberaba",
                "uf": "MG"
            }
            """;

        mockServer.expect(requestTo("https://viacep.com.br/ws/38060480/json/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        EnderecoResponseDTO resultado = viaCepService.buscarPorCep("38060-480");

        assertNotNull(resultado);
        assertEquals("38060-480", resultado.cep());
        assertEquals("Rua Novo Horizonte", resultado.logradouro());
        assertEquals("Uberaba", resultado.cidade());
        assertEquals("MG", resultado.uf());
        mockServer.verify();
    }

    @Test
    @DisplayName("Deve lançar exceção quando CEP tiver formato inválido")
    void deveLancarExcecaoQuandoCepInvalido() {
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () ->
                viaCepService.buscarPorCep("1234"));

        assertTrue(ex.getMessage().contains("CEP deve conter exatamente 8 dígitos numéricos"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CEP não for encontrado pelo ViaCEP")
    void deveLancarExcecaoQuandoCepNaoEncontrado() {
        String json = """
            {
                "erro": "true"
            }
            """;

        mockServer.expect(requestTo("https://viacep.com.br/ws/99999999/json/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                viaCepService.buscarPorCep("99999-999"));

        assertTrue(ex.getMessage().contains("CEP não encontrado"));
        mockServer.verify();
    }

    @Test
    @DisplayName("Deve buscar endereços por logradouro com sucesso")
    void deveBuscarPorLogradouroComSucesso() {
        String json = """
            [
                {
                    "cep": "38060-480",
                    "logradouro": "Rua Novo Horizonte",
                    "complemento": "",
                    "bairro": "Vila Santa Maria",
                    "localidade": "Uberaba",
                    "uf": "MG"
                }
            ]
            """;

        mockServer.expect(requestTo("https://viacep.com.br/ws/MG/Uberaba/Novo%20Horizonte/json/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<EnderecoResponseDTO> lista = viaCepService.buscarPorLogradouro("MG", "Uberaba", "Novo Horizonte");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Uberaba", lista.get(0).cidade());
        mockServer.verify();
    }

    @Test
    @DisplayName("Deve validar parâmetros insuficientes na busca por logradouro")
    void deveValidarParametrosInsuficientesNaBuscaPorLogradouro() {
        assertThrows(BusinessRuleException.class, () ->
                viaCepService.buscarPorLogradouro("M", "Uberaba", "Novo Horizonte"));

        assertThrows(BusinessRuleException.class, () ->
                viaCepService.buscarPorLogradouro("MG", "Ub", "Novo Horizonte"));

        assertThrows(BusinessRuleException.class, () ->
                viaCepService.buscarPorLogradouro("MG", "Uberaba", "ab"));
    }
}
