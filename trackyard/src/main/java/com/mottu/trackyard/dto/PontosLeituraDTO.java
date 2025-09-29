package com.mottu.trackyard.dto;

import com.mottu.trackyard.validation.ValidPontoLeitura;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PontosLeituraDTO(
    Long idPonto,

    @NotNull(message = "Pátio é obrigatório")
    Long idPatio,

    @NotBlank(message = "Nome do ponto é obrigatório")
    @ValidPontoLeitura
    String nomePonto,

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    String descricao
) {}