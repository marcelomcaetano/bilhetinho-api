package br.com.mmc.bilhetinho_api.service;

import br.com.mmc.bilhetinho_api.dto.EnderecoResponseDTO;
import br.com.mmc.bilhetinho_api.dto.ViaCepResponseDTO;
import br.com.mmc.bilhetinho_api.exception.BusinessRuleException;
import br.com.mmc.bilhetinho_api.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

/**
 * Serviço responsável pelo consumo, validação e tratamento das respostas da API externa do ViaCEP.
 */
@Slf4j
@Service
public class ViaCepService {

    private final RestClient viaCepRestClient;

    public ViaCepService(@Qualifier("viaCepRestClient") RestClient viaCepRestClient) {
        this.viaCepRestClient = viaCepRestClient;
    }

    /**
     * Consulta um endereço a partir do CEP.
     *
     * @param cep CEP com ou sem pontuação
     * @return EnderecoResponseDTO normalizado
     */
    public EnderecoResponseDTO buscarPorCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new BusinessRuleException("O CEP informado não pode ser nulo ou vazio.");
        }

        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) {
            throw new BusinessRuleException("CEP deve conter exatamente 8 dígitos numéricos.");
        }

        try {
            ViaCepResponseDTO response = viaCepRestClient.get()
                    .uri("/{cep}/json/", cepLimpo)
                    .retrieve()
                    .body(ViaCepResponseDTO.class);

            if (response == null || response.hasError() || response.cep() == null) {
                throw new ResourceNotFoundException("CEP não encontrado: " + cep);
            }

            return EnderecoResponseDTO.fromViaCep(response);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao consultar serviço ViaCEP para o CEP {}: {}", cep, e.getMessage(), e);
            throw new BusinessRuleException("Serviço de consulta de CEP temporariamente indisponível. Tente novamente mais tarde.");
        }
    }

    /**
     * Realiza a busca de endereços a partir de UF, cidade e logradouro.
     *
     * @param uf Sigla da Unidade Federativa (2 caracteres)
     * @param cidade Nome da cidade (mínimo 3 caracteres)
     * @param logradouro Nome ou trecho do logradouro (mínimo 3 caracteres)
     * @return Lista de EnderecoResponseDTO correspondentes
     */
    public List<EnderecoResponseDTO> buscarPorLogradouro(String uf, String cidade, String logradouro) {
        if (uf == null || uf.trim().length() != 2) {
            throw new BusinessRuleException("A UF é obrigatória e deve conter exatamente 2 letras (ex: MG, SP, RJ).");
        }
        if (cidade == null || cidade.trim().length() < 3) {
            throw new BusinessRuleException("A cidade é obrigatória e deve conter no mínimo 3 caracteres.");
        }
        if (logradouro == null || logradouro.trim().length() < 3) {
            throw new BusinessRuleException("O logradouro é obrigatório e deve conter no mínimo 3 caracteres.");
        }

        try {
            ViaCepResponseDTO[] response = viaCepRestClient.get()
                    .uri("/{uf}/{cidade}/{logradouro}/json/", uf.trim().toUpperCase(), cidade.trim(), logradouro.trim())
                    .retrieve()
                    .body(ViaCepResponseDTO[].class);

            if (response == null || response.length == 0) {
                return List.of();
            }

            return Arrays.stream(response)
                    .filter(r -> r != null && !r.hasError())
                    .map(EnderecoResponseDTO::fromViaCep)
                    .toList();
        } catch (Exception e) {
            log.error("Erro ao consultar serviço ViaCEP para UF={}, Cidade={}, Logradouro={}: {}", uf, cidade, logradouro, e.getMessage(), e);
            throw new BusinessRuleException("Serviço de busca de endereço temporariamente indisponível. Tente novamente mais tarde.");
        }
    }
}
