package br.com.mmc.bilhetinho_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EventoEnderecoDTO(
    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Formato de CEP inválido")
    String cep,

    @NotBlank(message = "O logradouro é obrigatório")
    String logradouro,

    @NotBlank(message = "O número é obrigatório")
    String numero,

    String complemento,

    @NotBlank(message = "O bairro é obrigatório")
    String bairro,

    @NotBlank(message = "A cidade é obrigatória")
    String cidade,

    @NotBlank(message = "A UF é obrigatória")
    @Size(min = 2, max = 2, message = "A UF deve conter 2 letras")
    String uf
) {}
