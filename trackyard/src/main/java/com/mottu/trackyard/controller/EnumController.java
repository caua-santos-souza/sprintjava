package com.mottu.trackyard.controller;

import com.mottu.trackyard.enums.ModeloMoto;
import com.mottu.trackyard.enums.PontoLeitura;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

    @GetMapping("/modelos-motos")
    public List<String> getModelosMotos() {
        return Arrays.stream(ModeloMoto.values())
                .map(ModeloMoto::getDescricao)
                .toList();
    }

    @GetMapping("/pontos-leitura")
    public List<String> getPontosLeitura() {
        return Arrays.stream(PontoLeitura.values())
                .map(PontoLeitura::getDescricao)
                .toList();
    }
}
