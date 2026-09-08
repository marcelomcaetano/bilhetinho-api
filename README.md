# Bilhetinho — API

> MVP desenvolvido para a disciplina de **Engenharia de Software**  
> Pós-Graduação em Engenharia de Software — PUC-Rio

---

## Autor

Marcelo M. Caetano  
[https://www.linkedin.com/in/marcelomcaetano/](https://www.linkedin.com/in/marcelomcaetano/)  

---

## O que este projeto representa

O **Bilhetinho API** representa o módulo de **Serviço de Negócios e Persistência de Dados (Back-End)** do sistema Bilhetinho. Ele atua como o motor central da aplicação, operando de forma autônoma para garantir a integridade de todas as operações e o armazenamento seguro das informações.

### Responsabilidades principais deste projeto:

1. **Gestão de Músicos e Apresentações:**
   * Cadastro e identificação de músicos.
   * Criação e ciclo de vida de eventos/shows ao vivo (`PENDENTE`, `ATIVO`, `ENCERRADO`).
   * Geração do código único do evento (`cod_evento` em UUID) utilizado para gerar o QR Code no local do show.

2. **Gestão do Endereço dos Eventos:**
   * Armazenamento da localização física do show (logradouro, bairro, cidade, uf, cep), alimentado com suporte da API do ViaCEP.
   * Suporte a buscas públicas por cidade e data.

3. **Gerenciamento do Fluxo de Pedidos (Bilhetinhos):**
   * Recebimento de solicitações com nome da música, artista, solicitante e mensagem.
   * Fila de pedidos para o músico com atualização de status (`PENDENTE`, `ACEITO`, `REJEITADO`).
   * Cancelamento e exclusão de pedidos via endpoint REST.

---

## Modelo de Dados (Tabelas do Banco)

* **`musico`:** Cadastro de artistas (`id`, `nome`, `email`, `estilo_musical`).
* **`evento`:** Shows ao vivo (`id`, `id_musico`, `cod_evento`, `nome`, `data_hora`, `local`, `status`).
* **`evento_endereco`:** Endereço do evento (`id`, `id_evento`, `cep`, `logradouro`, `numero`, `complemento`, `bairro`, `cidade`, `uf`).
* **`bilhetinho`:** Pedidos de música (`id`, `id_evento`, `id_musico`, `musica`, `artista`, `nome_solicitante`, `mensagem`, `data_hora`, `status`).

---

## Documentação Interativa da API (Swagger UI)

Com a aplicação rodando, a documentação interativa e os testes de todas as rotas estão disponíveis em:
* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI JSON:** `http://localhost:8080/api-docs`
