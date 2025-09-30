package com.mottu.trackyard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record MovimentacoesDTO(
    Long idMovimentacao,

    @NotBlank(message = "ID da moto é obrigatório")
    String idMoto,

    @NotNull(message = "ID do ponto é obrigatório")
    Long idPonto,

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    LocalDateTime dataHora
) {}