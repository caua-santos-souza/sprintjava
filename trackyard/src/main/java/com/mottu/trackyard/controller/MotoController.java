package com.mottu.trackyard.controller;

import com.mottu.trackyard.dto.MotosDTO;
import com.mottu.trackyard.dto.MotoComPontoAtualDTO;
import com.mottu.trackyard.dto.MoverMotoDTO;
import com.mottu.trackyard.dto.MoverMotoSimplesDTO;
import com.mottu.trackyard.dto.MovimentacoesDTO;
import com.mottu.trackyard.service.MotoService;
import com.mottu.trackyard.service.MovimentacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/motos")
@Validated
public class MotoController {
    private final MotoService motoService;
    private final MovimentacaoService movimentacaoService;

    public MotoController(MotoService motoService, MovimentacaoService movimentacaoService) {
        this.motoService = motoService;
        this.movimentacaoService = movimentacaoService;
    }

    //Método POST para criar uma entidade moto
    @PostMapping
    public ResponseEntity<MotosDTO> createMoto(@Valid @RequestBody MotosDTO dto) {
        MotosDTO created = motoService.createMoto(dto);
        return ResponseEntity.status(201).body(created);
    }

    //Método GET para paginação das motos
    @GetMapping
    public ResponseEntity<Page<MotosDTO>> getAllMotos(Pageable pageable) {
        return ResponseEntity.ok(motoService.getAllMotos(pageable));
    }

    //Método GET para exibir uma única moto através do ID
    @GetMapping("/{idMoto}")
    public ResponseEntity<MotosDTO> getMoto(@PathVariable String idMoto) {
        return ResponseEntity.ok(motoService.getMotoById(idMoto));
    }

    //Atualiza uma moto através do ID
    @PutMapping("/{idMoto}")
    public ResponseEntity<MotosDTO> updateMoto(@PathVariable String idMoto, @Valid @RequestBody MotosDTO dto) {
        return ResponseEntity.ok(motoService.updateMoto(idMoto, dto));
    }

    //Deletar uma moto por ID
    @DeleteMapping("/{idMoto}")
    public ResponseEntity<String> deleteMoto(@PathVariable String idMoto) {
        motoService.deleteMoto(idMoto);
        return ResponseEntity.ok("Moto de ID: " + idMoto + " foi deletada com sucesso!");
    }

    //Método GET para exibir uma única moto através da placa com ponto atual
    @GetMapping("/placa/{placa}")
    public ResponseEntity<MotoComPontoAtualDTO> findByPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(motoService.findByPlaca(placa));
    }

    //Método PUT para atualizar moto por placa (para QR Code) com ponto atual
    @PutMapping("/placa/{placa}")
    public ResponseEntity<MotoComPontoAtualDTO> updateMotoByPlaca(@PathVariable String placa, @RequestBody @Valid MotosDTO dto) {
        System.out.println("DEBUG: Controller recebeu PUT /placa/" + placa);
        System.out.println("DEBUG: DTO no controller - idMoto: " + dto.idMoto() + ", modelo: " + dto.modelo() + ", placa: " + dto.placa() + ", ponto: " + dto.ponto());
        
        MotoComPontoAtualDTO resultado = motoService.updateMotoByPlaca(placa, dto);
        
        System.out.println("DEBUG: Controller retornando resultado - modelo: " + resultado.modelo() + ", ponto: " + resultado.ponto());
        return ResponseEntity.ok(resultado);
    }

    //Endpoint de teste para debug
    @GetMapping("/test/{placa}")
    public ResponseEntity<String> testEndpoint(@PathVariable String placa) {
        System.out.println("DEBUG: Test endpoint chamado para placa: " + placa);
        return ResponseEntity.ok("Test OK para placa: " + placa);
    }

    //Mover moto para outro ponto por placa (para QR Code)
    @PutMapping("/placa/{placa}/mover")
    public ResponseEntity<MotoComPontoAtualDTO> moverMotoPorPlaca(@PathVariable String placa, @RequestBody MoverMotoDTO dto) {
        System.out.println("DEBUG: Controller recebeu PUT /placa/" + placa + "/mover");
        System.out.println("DEBUG: Ponto destino: " + dto.idPontoDestino());
        
        MotoComPontoAtualDTO resultado = motoService.moverMotoPorPlaca(placa, dto);
        
        System.out.println("DEBUG: Controller retornando resultado - ponto: " + resultado.ponto());
        return ResponseEntity.ok(resultado);
    }

    //Mover moto para outro ponto usando nome do ponto (versão simples)
    @PutMapping("/placa/{placa}/mover-para")
    public ResponseEntity<MotoComPontoAtualDTO> moverMotoPorPlacaSimples(@PathVariable String placa, @RequestBody MoverMotoSimplesDTO dto) {
        System.out.println("DEBUG: Controller recebeu PUT /placa/" + placa + "/mover-para");
        System.out.println("DEBUG: Ponto destino (nome): " + dto.nomePontoDestino());
        
        MotoComPontoAtualDTO resultado = motoService.moverMotoPorPlacaSimples(placa, dto);
        
        System.out.println("DEBUG: Controller retornando resultado - ponto: " + resultado.ponto());
        return ResponseEntity.ok(resultado);
    }

    //Método GET para exibir o histórico de movimentações de uma determinada moto através do ID
    @GetMapping("/{idMoto}/historico")
    public ResponseEntity<Page<MovimentacoesDTO>> listarHistorico(@PathVariable String idMoto, Pageable pageable) {
        return ResponseEntity.ok(movimentacaoService.getHistoricoMoto(idMoto, pageable));
    }
}