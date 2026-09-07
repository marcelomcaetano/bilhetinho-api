# Bilhetinho — API

> MVP desenvolvido para a disciplina de **Engenharia de Software**  
> Pós-Graduação em Engenharia de Software — PUC-Rio

---

## Autor

Marcelo M. Caetano  
[https://www.linkedin.com/in/marcelomcaetano/](https://www.linkedin.com/in/marcelomcaetano/)  

---

## O que este projeto representa

O **Bilhetinho API** representa o módulo de **Serviço de Negócios e Persistência de Dados (Back-End)** do sistema Bilhetinho. Ele atua como o "motor central" da aplicação, operando de forma autônoma para garantir a integridade de todas as operações e o armazenamento seguro das informações.

### Responsabilidades principais deste projeto:

1. **Gestão de Apresentações e Eventos:**
   * Registra, atualiza e organiza as informações sobre os shows e apresentações ativas (identificação do artista, local do evento e status).

2. **Gerenciamento do Fluxo de Pedidos (Bilhetinhos):**
   * Recebe as solicitações de música enviadas pelo público.
   * Mantém a fila de pedidos organizada para o músico.
   * Controla e atualiza o ciclo de vida de cada bilhete (se foi aceito, se está sendo tocado, se foi concluído ou recusado).
   * Permite o cancelamento ou remoção de pedidos quando necessário.

3. **Disponibilização Confiável de Dados:**
   * Fornece um canal padronizado e seguro para que qualquer interface (seja web, mobile ou ferramentas de teste) possa consultar e sincronizar as informações em tempo real.
