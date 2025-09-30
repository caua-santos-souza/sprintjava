package com.mottu.trackyard.dto;

import com.mottu.trackyard.validation.ValidPontoLeitura;
import jakarta.validation.constraints.NotBlank;

public record MoverMotoSimplesDTO(
    @NotBlank(message = "Nome do ponto de destino é obrigatório")
    @ValidPontoLeitura
    String nomePontoDestino
) {}
