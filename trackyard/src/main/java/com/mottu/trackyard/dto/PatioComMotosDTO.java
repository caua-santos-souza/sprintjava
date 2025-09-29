package com.mottu.trackyard.dto;

import java.util.List;

public record PatioComMotosDTO(
    Long idPatio,
    String nome,
    String telefone,
    String endereco,
    List<MotoComPontoDTO> motos
) {}
