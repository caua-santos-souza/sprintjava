package com.mottu.trackyard.service;

import com.mottu.trackyard.dto.PatiosDTO;
import com.mottu.trackyard.entity.Patios;
import com.mottu.trackyard.repository.PatiosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatioService {

    private final PatiosRepository patiosRepository;

    public PatioService(PatiosRepository patiosRepository) {
        this.patiosRepository = patiosRepository;
    }

    //Cria um pátio
    public PatiosDTO createPatio(PatiosDTO dto) {
        Optional<Patios> existingPatio = patiosRepository.findById(dto.idPatio());
        if (existingPatio.isPresent()) {
            throw new RuntimeException("Pátio com ID " + dto.idPatio() + " já existe");
        }

        Patios patio = new Patios();
        patio.setIdPatio(dto.idPatio());
        patio.setNome(dto.nome());
        patio.setTelefone(dto.telefone());
        patio.setEndereco(dto.endereco());
        patiosRepository.save(patio);

        return new PatiosDTO(patio.getIdPatio(), patio.getNome(), patio.getTelefone(), patio.getEndereco());
    }

    //Atualiza determinado pátio
    public PatiosDTO updatePatio(Long idPatio, PatiosDTO dto) {
        Patios patio = patiosRepository.findById(idPatio)
            .orElseThrow(() -> new RuntimeException("Pátio não encontrado"));

        if (!idPatio.equals(dto.idPatio())) {
            throw new RuntimeException("ID do pátio no corpo da requisição não corresponde ao ID na URL");
        }

        patio.setNome(dto.nome());
        patio.setTelefone(dto.telefone());
        patio.setEndereco(dto.endereco());
        patiosRepository.save(patio);

        return new PatiosDTO(patio.getIdPatio(), patio.getNome(), patio.getTelefone(), patio.getEndereco());
    }

    
    //Pagina os pátios existentes
    public Page<PatiosDTO> getAllPatios(Pageable pageable) {
        return patiosRepository.findAll(pageable)
            .map(patio -> new PatiosDTO(patio.getIdPatio(), patio.getNome(), patio.getTelefone(), patio.getEndereco()));
    }

    //Busca determinado pátio
    public PatiosDTO getPatioById(Long idPatio) {
        Patios patio = patiosRepository.findById(idPatio)
            .orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
        return new PatiosDTO(patio.getIdPatio(), patio.getNome(), patio.getTelefone(), patio.getEndereco());
    }

    //Deleta determinado pátio
    public void deletePatio(Long idPatio) {
        Patios patio = patiosRepository.findById(idPatio)
            .orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
        patiosRepository.delete(patio);
    }
}