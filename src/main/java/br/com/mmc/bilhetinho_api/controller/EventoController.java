package br.com.mmc.bilhetinho_api.controller;

import br.com.mmc.bilhetinho_api.dto.EventoRequestDTO;
import br.com.mmc.bilhetinho_api.dto.EventoResponseDTO;
import br.com.mmc.bilhetinho_api.dto.StatusEventoUpdateDTO;
import br.com.mmc.bilhetinho_api.model.StatusEvento;
import br.com.mmc.bilhetinho_api.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Eventos", description = "Endpoints para gerenciamento de shows, QR Code e endereços")
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar um novo show/evento com endereço")
    public ResponseEntity<EventoResponseDTO> criar(@Valid @RequestBody EventoRequestDTO dto) {
        EventoResponseDTO criado = eventoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar eventos ativos (com filtro opcional por cidade)")
    public ResponseEntity<List<EventoResponseDTO>> listarAtivos(
            @RequestParam(required = false) String cidade) {
        if (cidade != null && !cidade.isBlank()) {
            return ResponseEntity.ok(eventoService.listarPorCidadeEStatus(cidade, StatusEvento.ATIVO));
        }
        return ResponseEntity.ok(eventoService.listarAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID")
    public ResponseEntity<EventoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.buscarPorId(id));
    }

    @GetMapping("/codigo/{codEvento}")
    @Operation(summary = "Buscar evento pelo código UUID do QR Code")
    public ResponseEntity<EventoResponseDTO> buscarPorCodEvento(@PathVariable UUID codEvento) {
        return ResponseEntity.ok(eventoService.buscarPorCodEvento(codEvento));
    }

    @GetMapping("/musico/{musicoId}")
    @Operation(summary = "Listar todos os eventos de um músico")
    public ResponseEntity<List<EventoResponseDTO>> listarPorMusico(@PathVariable Long musicoId) {
        return ResponseEntity.ok(eventoService.listarPorMusico(musicoId));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status do evento (ATIVO, ENCERRADO, etc.)")
    public ResponseEntity<EventoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusEventoUpdateDTO dto) {
        return ResponseEntity.ok(eventoService.atualizarStatus(id, dto));
    }
}
