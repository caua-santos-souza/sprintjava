package com.mottu.trackyard.repository;

import com.mottu.trackyard.entity.PontosLeitura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PontosLeituraRepository extends JpaRepository<PontosLeitura, Long> {
    List<PontosLeitura> findByPatioIdPatio(Long idPatio);
}