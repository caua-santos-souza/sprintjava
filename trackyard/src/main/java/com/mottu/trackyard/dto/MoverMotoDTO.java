package com.mottu.trackyard.dto;

import com.mottu.trackyard.validation.ValidPontoLeitura;
import jakarta.validation.constraints.NotNull;

public record MoverMotoDTO(
    @NotNull(message = "ID do ponto de destino é obrigatório")
    Long idPontoDestino
) {}
