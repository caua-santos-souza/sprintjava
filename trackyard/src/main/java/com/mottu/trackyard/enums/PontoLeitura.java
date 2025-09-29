package com.mottu.trackyard.enums;

public enum PontoLeitura {
    DEFEITO_MOTOR("defeito motor"),
    DANO_ESTRUTURAL("dano estrutural"),
    MINHA_MOTTU("minha mottu"),
    AGENDAMENTO("agendamento"),
    PENDENCIA("pendência"),
    REPAROS_SIMPLES("reparos simples"),
    PARA_ALUGAR("para alugar"),
    SEM_PLACA("sem placa");

    private final String descricao;

    PontoLeitura(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
