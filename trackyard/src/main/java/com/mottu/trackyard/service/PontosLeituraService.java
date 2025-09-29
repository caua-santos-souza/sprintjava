package com.mottu.trackyard.service;

import com.mottu.trackyard.dto.PontosLeituraDTO;
import com.mottu.trackyard.entity.Patios;
import com.mottu.trackyard.entity.PontosLeitura;
import com.mottu.trackyard.repository.PatiosRepository;
import com.mottu.trackyard.repository.PontosLeituraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PontosLeituraService {

    private final PontosLeituraRepository pontosLeituraRepository;
    private final PatiosRepository patiosRepository;

    public PontosLeituraService(PontosLeituraRepository pontosLeituraRepository, PatiosRepository patiosRepository) {
        this.pontosLeituraRepository = pontosLeituraRepository;
        this.patiosRepository = patiosRepository;
    }

    //Cria um ponto de leitura dento de determinado pátio
    public PontosLeituraDTO createPontoLeitura(PontosLeituraDTO dto) {
        Optional<Patios> patio = patiosRepository.findById(dto.idPatio());
        if (patio.isEmpty()) {
            throw new RuntimeException("Pátio não encontrado");
        }

        PontosLeitura ponto = new PontosLeitura();
        ponto.setIdPonto(dto.idPonto());
        ponto.setPatio(patio.get());
        ponto.setNomePonto(dto.nomePonto());
        ponto.setDescricao(dto.descricao());
        pontosLeituraRepository.save(ponto);

        return new PontosLeituraDTO(ponto.getIdPonto(), ponto.getPatio().getIdPatio(), ponto.getNomePonto(), ponto.getDescricao());
    }

    //Atualiza determinado ponto de leitura
    public PontosLeituraDTO updatePontoLeitura(Long idPonto, PontosLeituraDTO dto) {
        PontosLeitura ponto = pontosLeituraRepository.findById(idPonto)
            .orElseThrow(() -> new RuntimeException("Ponto de leitura não encontrado"));

        if (!idPonto.equals(dto.idPonto())) {
            throw new RuntimeException("ID do ponto de leitura no corpo da requisição não corresponde ao ID na URL");
        }

        Optional<Patios> patio = patiosRepository.findById(dto.idPatio());
        if (patio.isEmpty()) {
            throw new RuntimeException("Pátio não encontrado");
        }

        ponto.setPatio(patio.get());
        ponto.setNomePonto(dto.nomePonto());
        ponto.setDescricao(dto.descricao());
        pontosLeituraRepository.save(ponto);

        return new PontosLeituraDTO(ponto.getIdPonto(), ponto.getPatio().getIdPatio(), ponto.getNomePonto(), ponto.getDescricao());
    }

    //Pagina pontos de leitura
    public Page<PontosLeituraDTO> getAllPontosLeitura(Pageable pageable) {
        return pontosLeituraRepository.findAll(pageable)
            .map(ponto -> new PontosLeituraDTO(ponto.getIdPonto(), ponto.getPatio().getIdPatio(), ponto.getNomePonto(), ponto.getDescricao()));
    }

    //Busca um ponto de leitura
    public PontosLeituraDTO getPontoLeituraById(Long idPonto) {
        PontosLeitura ponto = pontosLeituraRepository.findById(idPonto)
            .orElseThrow(() -> new RuntimeException("Ponto de leitura não encontrado"));
        return new PontosLeituraDTO(ponto.getIdPonto(), ponto.getPatio().getIdPatio(), ponto.getNomePonto(), ponto.getDescricao());
    }

    //Deleta um ponto de leitura
    public void deletePontoLeitura(Long idPonto) {
        PontosLeitura ponto = pontosLeituraRepository.findById(idPonto)
            .orElseThrow(() -> new RuntimeException("Ponto de leitura não encontrado"));
        pontosLeituraRepository.delete(ponto);
    }
}