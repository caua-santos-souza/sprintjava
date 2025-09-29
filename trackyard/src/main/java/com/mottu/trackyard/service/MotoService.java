package com.mottu.trackyard.service;

import com.mottu.trackyard.dto.MotosDTO;
import com.mottu.trackyard.entity.Motos;
import com.mottu.trackyard.repository.MotosRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MotoService {

    private final MotosRepository motosRepository;

    public MotoService(MotosRepository motosRepository) {
        this.motosRepository = motosRepository;
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

    //Exibe uma determinada moto por placa
    public MotosDTO findByPlaca(String placa) {
        Motos moto = motosRepository.findByPlaca(placa);
        if (moto == null) {
            throw new RuntimeException("Moto não encontrada com a placa: " + placa);
        }
        return new MotosDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca());
    }
}