package br.com.mmc.bilhetinho_api.controller;

import br.com.mmc.bilhetinho_api.dto.MusicoRequestDTO;
import br.com.mmc.bilhetinho_api.dto.MusicoResponseDTO;
import br.com.mmc.bilhetinho_api.service.MusicoService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/musicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Músicos", description = "Endpoints para cadastro e consulta de músicos")
public class MusicoController {

    private final MusicoService musicoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar um novo músico")
    public ResponseEntity<MusicoResponseDTO> cadastrar(@Valid @RequestBody MusicoRequestDTO dto) {
        MusicoResponseDTO criado = musicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar todos os músicos")
    public ResponseEntity<List<MusicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(musicoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar músico por ID")
    public ResponseEntity<MusicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(musicoService.buscarPorId(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar músico por e-mail")
    public ResponseEntity<MusicoResponseDTO> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(musicoService.buscarPorEmail(email));
    }
}
