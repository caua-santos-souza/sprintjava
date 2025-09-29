package com.mottu.trackyard.enums;

public enum ModeloMoto {
    POP("Pop"),
    SPORT("Sport"),
    E("E");

    private final String descricao;

    ModeloMoto(String descricao) {
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
