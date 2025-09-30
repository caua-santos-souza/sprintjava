package com.mottu.trackyard.service;

import com.mottu.trackyard.dto.MotosDTO;
import com.mottu.trackyard.dto.MotoComPontoAtualDTO;
import com.mottu.trackyard.entity.Motos;
import com.mottu.trackyard.entity.Movimentacoes;
import com.mottu.trackyard.repository.MotosRepository;
import com.mottu.trackyard.repository.MovimentacoesRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoService {

    private final MotosRepository motosRepository;
    private final MovimentacoesRepository movimentacoesRepository;

    public MotoService(MotosRepository motosRepository, MovimentacoesRepository movimentacoesRepository) {
        this.motosRepository = motosRepository;
        this.movimentacoesRepository = movimentacoesRepository;
    }

    public MotosDTO createMoto(MotosDTO dto) {
        Motos moto = new Motos();
        moto.setIdMoto(dto.idMoto());
        moto.setModelo(dto.modelo());
        moto.setPlaca(dto.placa());
        motosRepository.save(moto);
        return new MotosDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca());
    }

    //Pagina todas as motos
    public Page<MotosDTO> getAllMotos(Pageable pageable) {
        return motosRepository.findAll(pageable)
            .map(moto -> new MotosDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca()));
    }

    //Exibe determinada moto
    @Cacheable(value = "motos", key = "#idMoto")
    public MotosDTO getMotoById(String idMoto) {
        Motos moto = motosRepository.findById(idMoto)
            .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        return new MotosDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca());
    }

    //Atualiza determinada moto
    @CacheEvict(value = "motos", key = "#idMoto")
    public MotosDTO updateMoto(String idMoto, MotosDTO dto) {
        Motos moto = motosRepository.findById(idMoto)
            .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        if (!idMoto.equals(dto.idMoto())) {
            throw new RuntimeException("ID da moto no corpo da requisição não corresponde ao ID na URL");
        }
        moto.setModelo(dto.modelo());
        moto.setPlaca(dto.placa());
        motosRepository.save(moto);
        return new MotosDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca());
    }

    //Deleta uma determinada moto
    @CacheEvict(value = "motos", key = "#idMoto")
    public void deleteMoto(String idMoto) {
        Motos moto = motosRepository.findById(idMoto)
            .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        motosRepository.delete(moto);
    }

    //Exibe uma determinada moto por placa com ponto atual
    public MotoComPontoAtualDTO findByPlaca(String placa) {
        Motos moto = motosRepository.findByPlaca(placa);
        if (moto == null) {
            throw new RuntimeException("Moto não encontrada com a placa: " + placa);
        }
        
        // Buscar a movimentação mais recente para obter o ponto atual
        String pontoAtual = obterPontoAtual(moto.getIdMoto());
        
        return new MotoComPontoAtualDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca(), pontoAtual);
    }

    //Atualiza uma moto por placa (para QR Code) com ponto atual
    @CacheEvict(value = "motos", allEntries = true)
    public MotoComPontoAtualDTO updateMotoByPlaca(String placa, MotosDTO dto) {
        Motos moto = motosRepository.findByPlaca(placa);
        if (moto == null) {
            throw new RuntimeException("Moto não encontrada com a placa: " + placa);
        }
        
        // Atualiza apenas o modelo (placa não pode ser alterada)
        moto.setModelo(dto.modelo());
        motosRepository.save(moto);
        
        // Buscar o ponto atual após a atualização
        String pontoAtual = obterPontoAtual(moto.getIdMoto());
        
        return new MotoComPontoAtualDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca(), pontoAtual);
    }

    //Método auxiliar para obter o ponto atual da moto
    private String obterPontoAtual(String idMoto) {
        List<Movimentacoes> movimentacoes = movimentacoesRepository.findByMotoIdMoto(idMoto);
        if (movimentacoes.isEmpty()) {
            return "Sem movimentação";
        }
        
        // Retorna o ponto da movimentação mais recente
        Movimentacoes ultimaMovimentacao = movimentacoes.stream()
            .max((m1, m2) -> m1.getDataHora().compareTo(m2.getDataHora()))
            .orElse(null);
            
        return ultimaMovimentacao != null ? ultimaMovimentacao.getPontoLeitura().getNomePonto() : "Sem movimentação";
    }
}