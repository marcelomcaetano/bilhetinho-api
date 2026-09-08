package br.com.mmc.bilhetinho_api.controller;

import br.com.mmc.bilhetinho_api.dto.BilhetinhoRequestDTO;
import br.com.mmc.bilhetinho_api.dto.BilhetinhoResponseDTO;
import br.com.mmc.bilhetinho_api.dto.StatusBilhetinhoUpdateDTO;
import br.com.mmc.bilhetinho_api.service.BilhetinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bilhetinhos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Bilhetinhos", description = "Endpoints para pedidos de música pelo público e gestão pelo músico")
public class BilhetinhoController {

    private final BilhetinhoService bilhetinhoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Enviar um novo pedido de música (bilhetinho) para um evento")
    public ResponseEntity<BilhetinhoResponseDTO> criar(@Valid @RequestBody BilhetinhoRequestDTO dto) {
        BilhetinhoResponseDTO criado = bilhetinhoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pedido por ID")
    public ResponseEntity<BilhetinhoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bilhetinhoService.buscarPorId(id));
    }

    @GetMapping("/evento/{eventoId}")
    @Operation(summary = "Listar fila de pedidos de um evento (ordem cronológica)")
    public ResponseEntity<List<BilhetinhoResponseDTO>> listarPorEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(bilhetinhoService.listarPorEvento(eventoId));
    }

    @GetMapping("/musico/{musicoId}")
    @Operation(summary = "Listar todos os pedidos recebidos por um músico")
    public ResponseEntity<List<BilhetinhoResponseDTO>> listarPorMusico(@PathVariable Long musicoId) {
        return ResponseEntity.ok(bilhetinhoService.listarPorMusico(musicoId));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido (ACEITO, REJEITADO)")
    public ResponseEntity<BilhetinhoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusBilhetinhoUpdateDTO dto) {
        return ResponseEntity.ok(bilhetinhoService.atualizarStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir ou cancelar um bilhetinho")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bilhetinhoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
