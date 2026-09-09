package br.com.mmc.bilhetinho_api.controller;

import br.com.mmc.bilhetinho_api.dto.EnderecoResponseDTO;
import br.com.mmc.bilhetinho_api.dto.ErrorResponseDTO;
import br.com.mmc.bilhetinho_api.service.ViaCepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller RESTful para consulta e busca de endereços integrados com a API externa ViaCEP.
 */
@RestController
@RequestMapping("/api/enderecos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Endereços", description = "Endpoints para consulta e normalização de endereços via integração externa com o ViaCEP")
public class EnderecoController {

    private final ViaCepService viaCepService;

    @GetMapping("/cep/{cep}")
    @Operation(
        summary = "Busca endereço por CEP",
        description = "Consulta o CEP na API externa ViaCEP, normaliza os dados e retorna as informações de logradouro, bairro, cidade e estado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Formato de CEP inválido",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "CEP não encontrado na base de dados",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<EnderecoResponseDTO> buscarPorCep(
            @Parameter(description = "CEP a ser consultado (com ou sem hífen, ex: 38060-480 ou 38060480)", example = "38060-480")
            @PathVariable String cep) {
        EnderecoResponseDTO endereco = viaCepService.buscarPorCep(cep);
        return ResponseEntity.ok(endereco);
    }

    @GetMapping("/busca")
    @Operation(
        summary = "Busca endereços por UF, Cidade e Logradouro",
        description = "Realiza busca reversa de endereços na API externa ViaCEP a partir do nome da rua ou trecho do logradouro."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de endereços correspondentes encontrados"),
        @ApiResponse(responseCode = "400", description = "Parâmetros de busca inválidos ou insuficientes",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<List<EnderecoResponseDTO>> buscarPorLogradouro(
            @Parameter(description = "Sigla do estado (UF) com 2 letras (ex: MG, RJ, SP)", example = "MG")
            @RequestParam String uf,
            @Parameter(description = "Nome da cidade (mínimo 3 caracteres)", example = "Uberaba")
            @RequestParam String cidade,
            @Parameter(description = "Nome do logradouro ou trecho (mínimo 3 caracteres)", example = "Novo Horizonte")
            @RequestParam String logradouro) {
        List<EnderecoResponseDTO> enderecos = viaCepService.buscarPorLogradouro(uf, cidade, logradouro);
        return ResponseEntity.ok(enderecos);
    }
}
