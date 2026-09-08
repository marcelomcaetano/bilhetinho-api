package br.com.mmc.bilhetinho_api.service;

import br.com.mmc.bilhetinho_api.dto.EventoRequestDTO;
import br.com.mmc.bilhetinho_api.dto.EventoResponseDTO;
import br.com.mmc.bilhetinho_api.dto.StatusEventoUpdateDTO;
import br.com.mmc.bilhetinho_api.exception.ResourceNotFoundException;
import br.com.mmc.bilhetinho_api.mapper.EventoMapper;
import br.com.mmc.bilhetinho_api.model.Evento;
import br.com.mmc.bilhetinho_api.model.Musico;
import br.com.mmc.bilhetinho_api.model.StatusEvento;
import br.com.mmc.bilhetinho_api.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final MusicoService musicoService;

    @Transactional
    public EventoResponseDTO criar(EventoRequestDTO dto) {
        Musico musico = musicoService.buscarEntidadePorId(dto.idMusico());
        Evento evento = EventoMapper.toEntity(dto, musico);
        Evento salvo = eventoRepository.save(evento);
        return EventoMapper.toDTO(salvo);
    }

    @Transactional(readOnly = true)
    public EventoResponseDTO buscarPorId(Long id) {
        Evento evento = buscarEntidadePorId(id);
        return EventoMapper.toDTO(evento);
    }

    @Transactional(readOnly = true)
    public EventoResponseDTO buscarPorCodEvento(UUID codEvento) {
        Evento evento = buscarEntidadePorCodEvento(codEvento);
        return EventoMapper.toDTO(evento);
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarPorMusico(Long musicoId) {
        return eventoRepository.findByMusicoIdOrderByDataHoraDesc(musicoId)
                .stream()
                .map(EventoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarAtivos() {
        return eventoRepository.findByStatusOrderByDataHoraAsc(StatusEvento.ATIVO)
                .stream()
                .map(EventoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarPorCidadeEStatus(String cidade, StatusEvento status) {
        StatusEvento statusFiltro = (status != null) ? status : StatusEvento.ATIVO;
        return eventoRepository.findByCidadeAndStatus(cidade, statusFiltro)
                .stream()
                .map(EventoMapper::toDTO)
                .toList();
    }

    @Transactional
    public EventoResponseDTO atualizarStatus(Long id, StatusEventoUpdateDTO dto) {
        Evento evento = buscarEntidadePorId(id);
        evento.setStatus(dto.status());
        Evento atualizado = eventoRepository.save(evento);
        return EventoMapper.toDTO(atualizado);
    }

    @Transactional(readOnly = true)
    public Evento buscarEntidadePorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Evento buscarEntidadePorCodEvento(UUID codEvento) {
        return eventoRepository.findByCodEvento(codEvento)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com o código informado: " + codEvento));
    }
}
