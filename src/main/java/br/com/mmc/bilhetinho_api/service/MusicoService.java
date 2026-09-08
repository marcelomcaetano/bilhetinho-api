package br.com.mmc.bilhetinho_api.service;

import br.com.mmc.bilhetinho_api.dto.MusicoRequestDTO;
import br.com.mmc.bilhetinho_api.dto.MusicoResponseDTO;
import br.com.mmc.bilhetinho_api.exception.BusinessRuleException;
import br.com.mmc.bilhetinho_api.exception.ResourceNotFoundException;
import br.com.mmc.bilhetinho_api.mapper.MusicoMapper;
import br.com.mmc.bilhetinho_api.model.Musico;
import br.com.mmc.bilhetinho_api.repository.MusicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicoService {

    private final MusicoRepository musicoRepository;

    @Transactional
    public MusicoResponseDTO cadastrar(MusicoRequestDTO dto) {
        // Regra de negócio: impede o cadastro de mais de um músico com o mesmo e-mail
        if (musicoRepository.existsByEmail(dto.email())) {
            throw new BusinessRuleException("Já existe um músico cadastrado com o e-mail: " + dto.email());
        }

        Musico musico = MusicoMapper.toEntity(dto);
        Musico salvo = musicoRepository.save(musico);
        return MusicoMapper.toDTO(salvo);
    }

    @Transactional(readOnly = true)
    public MusicoResponseDTO buscarPorId(Long id) {
        Musico musico = buscarEntidadePorId(id);
        return MusicoMapper.toDTO(musico);
    }

    @Transactional(readOnly = true)
    public MusicoResponseDTO buscarPorEmail(String email) {
        Musico musico = musicoRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Músico não encontrado com o e-mail: " + email));
        return MusicoMapper.toDTO(musico);
    }

    @Transactional(readOnly = true)
    public List<MusicoResponseDTO> listarTodos() {
        return musicoRepository.findAll()
                .stream()
                .map(MusicoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Musico buscarEntidadePorId(Long id) {
        return musicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Músico não encontrado com ID: " + id));
    }
}
