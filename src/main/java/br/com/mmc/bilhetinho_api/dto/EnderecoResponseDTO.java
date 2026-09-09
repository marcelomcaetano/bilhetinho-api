package br.com.mmc.bilhetinho_api.dto;

/**
 * DTO normalizado para retorno de informações de endereço ao front-end.
 * O campo 'cidade' é padronizado a partir do campo 'localidade' do ViaCEP.
 */
public record EnderecoResponseDTO(
    String cep,
    String logradouro,
    String complemento,
    String bairro,
    String cidade,
    String uf
) {
    public static EnderecoResponseDTO fromViaCep(ViaCepResponseDTO viaCep) {
        return new EnderecoResponseDTO(
            viaCep.cep(),
            viaCep.logradouro() != null ? viaCep.logradouro() : "",
            viaCep.complemento() != null ? viaCep.complemento() : "",
            viaCep.bairro() != null ? viaCep.bairro() : "",
            viaCep.localidade() != null ? viaCep.localidade() : "",
            viaCep.uf() != null ? viaCep.uf() : ""
        );
    }
}
