package br.com.mmc.bilhetinho_api.service;

import br.com.mmc.bilhetinho_api.dto.BilhetinhoRequestDTO;
import br.com.mmc.bilhetinho_api.dto.BilhetinhoResponseDTO;
import br.com.mmc.bilhetinho_api.dto.StatusBilhetinhoUpdateDTO;
import br.com.mmc.bilhetinho_api.exception.BusinessRuleException;
import br.com.mmc.bilhetinho_api.exception.ResourceNotFoundException;
import br.com.mmc.bilhetinho_api.mapper.BilhetinhoMapper;
import br.com.mmc.bilhetinho_api.model.Bilhetinho;
import br.com.mmc.bilhetinho_api.model.Evento;
import br.com.mmc.bilhetinho_api.model.StatusBilhetinho;
import br.com.mmc.bilhetinho_api.model.StatusEvento;
import br.com.mmc.bilhetinho_api.repository.BilhetinhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BilhetinhoService {

    private final BilhetinhoRepository bilhetinhoRepository;
    private final EventoService eventoService;

    @Transactional
    public BilhetinhoResponseDTO criar(BilhetinhoRequestDTO dto) {
        Evento evento = eventoService.buscarEntidadePorCodEvento(dto.codEvento());

        // Regra de negócio: pedidos de música só são aceitos quando o evento estiver com status ATIVO
        if (evento.getStatus() != StatusEvento.ATIVO) {
            throw new BusinessRuleException("Não é possível enviar pedidos para este evento, pois ele não está ativo no momento.");
        }

        Bilhetinho bilhetinho = BilhetinhoMapper.toEntity(dto, evento);
        Bilhetinho salvo = bilhetinhoRepository.save(bilhetinho);
        return BilhetinhoMapper.toDTO(salvo);
    }

    @Transactional(readOnly = true)
    public BilhetinhoResponseDTO buscarPorId(Long id) {
        Bilhetinho bilhetinho = buscarEntidadePorId(id);
        return BilhetinhoMapper.toDTO(bilhetinho);
    }

    @Transactional(readOnly = true)
    public List<BilhetinhoResponseDTO> listarPorEvento(Long eventoId) {
        return bilhetinhoRepository.findByEventoIdOrderByDataHoraDesc(eventoId)
                .stream()
                .map(BilhetinhoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BilhetinhoResponseDTO> listarPorMusico(Long musicoId) {
        return bilhetinhoRepository.findByMusicoIdOrderByDataHoraDesc(musicoId)
                .stream()
                .map(BilhetinhoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BilhetinhoResponseDTO> listarPorEventoEStatus(Long eventoId, StatusBilhetinho status) {
        return bilhetinhoRepository.findByEventoIdAndStatusOrderByDataHoraAsc(eventoId, status)
                .stream()
                .map(BilhetinhoMapper::toDTO)
                .toList();
    }

    @Transactional
    public BilhetinhoResponseDTO atualizarStatus(Long id, StatusBilhetinhoUpdateDTO dto) {
        Bilhetinho bilhetinho = buscarEntidadePorId(id);
        bilhetinho.setStatus(dto.status());
        Bilhetinho atualizado = bilhetinhoRepository.save(bilhetinho);
        return BilhetinhoMapper.toDTO(atualizado);
    }

    @Transactional
    public void excluir(Long id) {
        Bilhetinho bilhetinho = buscarEntidadePorId(id);
        bilhetinhoRepository.delete(bilhetinho);
    }

    @Transactional(readOnly = true)
    public Bilhetinho buscarEntidadePorId(Long id) {
        return bilhetinhoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bilhetinho não encontrado com ID: " + id));
    }
}
