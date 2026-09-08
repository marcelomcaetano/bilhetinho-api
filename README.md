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

### Responsabilidades principais deste projeto

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

## Camada de Persistência (Spring Data JPA)

* **`MusicoRepository`:** Operações de persistência e busca rápida por e-mail único (`findByEmail`).
* **`EventoRepository`:** Busca por UUID do QR Code (`findByCodEvento`), busca de shows do músico e filtros de eventos ativos por cidade e data.
* **`EventoEnderecoRepository`:** Gestão do endereço físico do evento integrado ao ViaCEP.
* **`BilhetinhoRepository`:** Gerenciamento da fila de pedidos por evento ordenada cronologicamente e contagem por status.

---

## Camada de Transferência de Dados (DTOs — Java Records)

Implementados como `records` do Java 21, assegurando imutabilidade, integridade estrutural e validações com Bean Validation (`jakarta.validation.constraints`):

* **`MusicoRequestDTO`:** Dados de entrada para cadastro do artista (`nome`, `email` com validação de formato e `estiloMusical`).
* **`MusicoResponseDTO`:** Retorno do músico cadastrado com seu identificador gerado (`id`).
* **`EventoEnderecoDTO`:** Estrutura de endereço padronizada para consumo do ViaCEP (`cep` formatado, `logradouro`, `numero`, `complemento`, `bairro`, `cidade`, `uf`).
* **`EventoRequestDTO`:** Entrada para criação de shows com validação em cascata (`@Valid`) do endereço e vínculos de músico.
* **`EventoResponseDTO`:** Retorno completo do evento para o front-end, incluindo `codEvento` (`UUID`) para exibição e geração do QR Code.
* **`StatusEventoUpdateDTO`:** Atualização do ciclo de vida do show (`PENDENTE`, `ATIVO`, `ENCERRADO`).
* **`BilhetinhoRequestDTO`:** Solicitação de música feita pelo público (`codEvento`, `musica`, `artista`, `nomeSolicitante`, `mensagem`).
* **`BilhetinhoResponseDTO`:** Retorno do pedido criado com timestamp, status e dados associados.
* **`StatusBilhetinhoUpdateDTO`:** Alteração do status do pedido pelo músico no palco (`PENDENTE`, `ACEITO`, `REJEITADO`).

---

## Camada de Negócios (Services & Exceptions)

A lógica central da aplicação é encapsulada em serviços transacionais com isolamento de regras e disparo de exceções HTTP padronizadas:

* **`MusicoService`:** Regra de unicidade de e-mail (`existsByEmail`), cadastro de novos músicos e consultas operacionais.
* **`EventoService`:** Geração automática do identificador universal (`UUID`) do QR Code, persistência atômica em cascata do endereço e consulta de shows ativos filtrados por cidade.
* **`BilhetinhoService`:** Validação de evento ativo ao receber pedidos do público, vínculo relacional automático com o músico, fila ordenada cronologicamente e operações de aceite, recusa e cancelamento.
* **`ResourceNotFoundException`:** Exceção com mapeamento automático para HTTP 404 (Not Found).
* **`BusinessRuleException`:** Exceção com mapeamento automático para HTTP 400 (Bad Request).

---

## Camada de Mapeamento (Mappers)

Implementadas seguindo o padrão de **Utility Classes** (construtor privado bloqueado contra instanciação, métodos puramente estáticos e *null-safety*):

* **`MusicoMapper`:** Conversão bidirecional entre `Musico` e `MusicoRequestDTO` / `MusicoResponseDTO`.
* **`EventoEnderecoMapper`:** Conversão entre `EventoEndereco` e `EventoEnderecoDTO`.
* **`EventoMapper`:** Montagem do agregado `Evento` vinculando entidade `Musico` e endereço em cascata, além da conversão para `EventoResponseDTO`.
* **`BilhetinhoMapper`:** Montagem do pedido associando `Evento` ativo e `Musico`, com geração de timestamp e conversão para `BilhetinhoResponseDTO`.

---

## Controladores REST e Rotas da API

A API implementa integralmente os 4 verbos HTTP exigidos (`GET`, `POST`, `PUT`, `DELETE`), com suporte universal a CORS (`@CrossOrigin(origins = "*")`) para integração com o Front-End:

### Músicos (`/api/musicos`)

| Verbo | Rota | Descrição | Retorno |
|---|---|---|---|
| `POST` | `/api/musicos` | Cadastra um novo músico | `201 Created` |
| `GET` | `/api/musicos` | Lista todos os músicos cadastrados | `200 OK` |
| `GET` | `/api/musicos/{id}` | Busca músico por identificador | `200 OK` |
| `GET` | `/api/musicos/email/{email}` | Busca músico por e-mail | `200 OK` |

### Eventos / Shows (`/api/eventos`)

| Verbo | Rota | Descrição | Retorno |
|---|---|---|---|
| `POST` | `/api/eventos` | Cria show com endereço integrado ao ViaCEP e gera UUID | `201 Created` |
| `GET` | `/api/eventos` | Lista shows ativos (filtro opcional: `?cidade=Rio`) | `200 OK` |
| `GET` | `/api/eventos/{id}` | Detalhes do show por ID | `200 OK` |
| `GET` | `/api/eventos/codigo/{codEvento}` | Localiza show pelo UUID do QR Code | `200 OK` |
| `GET` | `/api/eventos/musico/{musicoId}` | Lista shows de um determinado artista | `200 OK` |
| `PUT` | `/api/eventos/{id}/status` | Atualiza status do show (`ATIVO`, `ENCERRADO`) | `200 OK` |

### Bilhetinhos / Pedidos de Música (`/api/bilhetinhos`)

| Verbo | Rota | Descrição | Retorno |
|---|---|---|---|
| `POST` | `/api/bilhetinhos` | Envia pedido de música para um show ativo | `201 Created` |
| `GET` | `/api/bilhetinhos/{id}` | Consulta detalhes do pedido por ID | `200 OK` |
| `GET` | `/api/bilhetinhos/evento/{eventoId}` | Fila cronológica de pedidos do evento | `200 OK` |
| `GET` | `/api/bilhetinhos/musico/{musicoId}` | Histórico de pedidos recebidos pelo músico | `200 OK` |
| `PUT` | `/api/bilhetinhos/{id}/status` | Músico aceita ou recusa pedido (`ACEITO`, `REJEITADO`) | `200 OK` |
| `DELETE` | `/api/bilhetinhos/{id}` | Cancela e remove um bilhetinho | `204 No Content` |

---

Com a aplicação rodando, a documentação interativa e os testes de todas as rotas estão disponíveis em:

* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI JSON:** `http://localhost:8080/api-docs`
