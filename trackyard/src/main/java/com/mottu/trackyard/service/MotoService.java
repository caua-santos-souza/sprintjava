package com.mottu.trackyard.service;

import com.mottu.trackyard.dto.MotosDTO;
import com.mottu.trackyard.dto.MotoComPontoAtualDTO;
import com.mottu.trackyard.dto.MoverMotoDTO;
import com.mottu.trackyard.dto.MoverMotoSimplesDTO;
import com.mottu.trackyard.entity.Motos;
import com.mottu.trackyard.entity.Movimentacoes;
import com.mottu.trackyard.entity.PontosLeitura;
import com.mottu.trackyard.repository.MotosRepository;
import com.mottu.trackyard.repository.MovimentacoesRepository;
import com.mottu.trackyard.repository.PontosLeituraRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MotoService {

    private final MotosRepository motosRepository;
    private final MovimentacoesRepository movimentacoesRepository;
    private final PontosLeituraRepository pontosLeituraRepository;

    public MotoService(MotosRepository motosRepository, MovimentacoesRepository movimentacoesRepository, PontosLeituraRepository pontosLeituraRepository) {
        this.motosRepository = motosRepository;
        this.movimentacoesRepository = movimentacoesRepository;
        this.pontosLeituraRepository = pontosLeituraRepository;
    }

    public MotosDTO createMoto(MotosDTO dto) {
        Motos moto = new Motos();
        moto.setIdMoto(dto.idMoto());
        moto.setModelo(dto.modelo());
        moto.setPlaca(dto.placa());
        motosRepository.save(moto);
        return new MotosDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca());
    }

    //Pagina todas as motos
    public Page<MotosDTO> getAllMotos(Pageable pageable) {
        return motosRepository.findAll(pageable)
            .map(moto -> new MotosDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca()));
    }

    //Exibe determinada moto
    @Cacheable(value = "motos", key = "#idMoto")
    public MotosDTO getMotoById(String idMoto) {
        Motos moto = motosRepository.findById(idMoto)
            .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        return new MotosDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca());
    }

    //Atualiza determinada moto
    @CacheEvict(value = "motos", key = "#idMoto")
    public MotosDTO updateMoto(String idMoto, MotosDTO dto) {
        Motos moto = motosRepository.findById(idMoto)
            .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        if (!idMoto.equals(dto.idMoto())) {
            throw new RuntimeException("ID da moto no corpo da requisição não corresponde ao ID na URL");
        }
        moto.setModelo(dto.modelo());
        moto.setPlaca(dto.placa());
        motosRepository.save(moto);
        return new MotosDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca());
    }

    //Deleta uma determinada moto
    @CacheEvict(value = "motos", key = "#idMoto")
    public void deleteMoto(String idMoto) {
        Motos moto = motosRepository.findById(idMoto)
            .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        motosRepository.delete(moto);
    }

    //Exibe uma determinada moto por placa com ponto atual
    public MotoComPontoAtualDTO findByPlaca(String placa) {
        Motos moto = motosRepository.findByPlaca(placa);
        if (moto == null) {
            throw new RuntimeException("Moto não encontrada com a placa: " + placa);
        }
        
        // Buscar a movimentação mais recente para obter o ponto atual
        String pontoAtual = obterPontoAtual(moto.getIdMoto());
        
        return new MotoComPontoAtualDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca(), pontoAtual);
    }

    //Atualiza uma moto por placa (para QR Code) com ponto atual
    @CacheEvict(value = "motos", allEntries = true)
    public MotoComPontoAtualDTO updateMotoByPlaca(String placa, MotosDTO dto) {
        System.out.println("DEBUG: Iniciando updateMotoByPlaca para placa: " + placa);
        System.out.println("DEBUG: DTO recebido - modelo: " + dto.modelo() + ", placa: " + dto.placa() + ", ponto: " + dto.ponto());
        
        Motos moto = motosRepository.findByPlaca(placa);
        if (moto == null) {
            System.out.println("DEBUG: Moto não encontrada com placa: " + placa);
            throw new RuntimeException("Moto não encontrada com a placa: " + placa);
        }
        
        System.out.println("DEBUG: Moto encontrada - ID: " + moto.getIdMoto() + ", modelo atual: " + moto.getModelo());
        
        // Atualiza apenas o modelo (placa não pode ser alterada)
        String modeloAnterior = moto.getModelo();
        moto.setModelo(dto.modelo());
        System.out.println("DEBUG: Modelo alterado de '" + modeloAnterior + "' para '" + dto.modelo() + "'");
        
        Motos motoSalva = motosRepository.save(moto);
        System.out.println("DEBUG: Moto salva - modelo após save: " + motoSalva.getModelo());
        
        // Verificar se o ponto mudou
        String pontoAtual = obterPontoAtual(moto.getIdMoto());
        System.out.println("DEBUG: Ponto atual: " + pontoAtual);
        
        if (dto.ponto() != null && !dto.ponto().equals(pontoAtual)) {
            System.out.println("DEBUG: Ponto alterado de '" + pontoAtual + "' para '" + dto.ponto() + "'");
            
            // Buscar o ponto de destino pelo nome
            List<PontosLeitura> pontosComNome = pontosLeituraRepository.findByNomePonto(dto.ponto());
            if (pontosComNome.isEmpty()) {
                System.out.println("DEBUG: Ponto não encontrado: " + dto.ponto());
                throw new RuntimeException("Ponto '" + dto.ponto() + "' não encontrado");
            }
            
            // Se houver múltiplos pontos com o mesmo nome, pegar o primeiro
            PontosLeitura pontoDestino = pontosComNome.get(0);
            
            // Criar nova movimentação
            Movimentacoes movimentacao = new Movimentacoes();
            movimentacao.setMoto(moto);
            movimentacao.setPontoLeitura(pontoDestino);
            movimentacao.setDataHora(LocalDateTime.now());
            
            movimentacoesRepository.save(movimentacao);
            System.out.println("DEBUG: Movimentação registrada para ponto: " + dto.ponto());
            
            // Atualizar ponto atual
            pontoAtual = dto.ponto();
        } else {
            System.out.println("DEBUG: Ponto não alterado");
        }
        
        MotoComPontoAtualDTO resultado = new MotoComPontoAtualDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca(), pontoAtual);
        System.out.println("DEBUG: Resultado final - modelo: " + resultado.modelo() + ", ponto: " + resultado.ponto());
        
        return resultado;
    }

    //Método auxiliar para obter o ponto atual da moto
    private String obterPontoAtual(String idMoto) {
        List<Movimentacoes> movimentacoes = movimentacoesRepository.findByMotoIdMoto(idMoto);
        if (movimentacoes.isEmpty()) {
            return "Sem movimentação";
        }
        
        // Retorna o ponto da movimentação mais recente
        Movimentacoes ultimaMovimentacao = movimentacoes.stream()
            .max((m1, m2) -> m1.getDataHora().compareTo(m2.getDataHora()))
            .orElse(null);
            
        return ultimaMovimentacao != null ? ultimaMovimentacao.getPontoLeitura().getNomePonto() : "Sem movimentação";
    }

    //Move uma moto para outro ponto (para QR Code)
    @CacheEvict(value = "motos", allEntries = true)
    public MotoComPontoAtualDTO moverMotoPorPlaca(String placa, MoverMotoDTO dto) {
        System.out.println("DEBUG: Iniciando moverMotoPorPlaca para placa: " + placa);
        System.out.println("DEBUG: Ponto destino: " + dto.idPontoDestino());
        
        // Buscar a moto pela placa
        Motos moto = motosRepository.findByPlaca(placa);
        if (moto == null) {
            System.out.println("DEBUG: Moto não encontrada com placa: " + placa);
            throw new RuntimeException("Moto não encontrada com a placa: " + placa);
        }
        
        // Buscar o ponto de destino
        PontosLeitura pontoDestino = pontosLeituraRepository.findById(dto.idPontoDestino())
            .orElseThrow(() -> new RuntimeException("Ponto de destino não encontrado"));
        
        System.out.println("DEBUG: Moto encontrada - ID: " + moto.getIdMoto());
        System.out.println("DEBUG: Ponto destino: " + pontoDestino.getNomePonto());
        
        // Criar nova movimentação
        Movimentacoes movimentacao = new Movimentacoes();
        movimentacao.setMoto(moto);
        movimentacao.setPontoLeitura(pontoDestino);
        movimentacao.setDataHora(LocalDateTime.now());
        
        movimentacoesRepository.save(movimentacao);
        System.out.println("DEBUG: Movimentação registrada");
        
        // Retornar moto com novo ponto
        String novoPonto = pontoDestino.getNomePonto();
        System.out.println("DEBUG: Novo ponto: " + novoPonto);
        
        MotoComPontoAtualDTO resultado = new MotoComPontoAtualDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca(), novoPonto);
        System.out.println("DEBUG: Resultado final - ponto: " + resultado.ponto());
        
        return resultado;
    }

    //Move uma moto para outro ponto usando nome do ponto (versão simples)
    @CacheEvict(value = "motos", allEntries = true)
    public MotoComPontoAtualDTO moverMotoPorPlacaSimples(String placa, MoverMotoSimplesDTO dto) {
        System.out.println("DEBUG: Iniciando moverMotoPorPlacaSimples para placa: " + placa);
        System.out.println("DEBUG: Ponto destino (nome): " + dto.nomePontoDestino());
        
        // Buscar a moto pela placa
        Motos moto = motosRepository.findByPlaca(placa);
        if (moto == null) {
            System.out.println("DEBUG: Moto não encontrada com placa: " + placa);
            throw new RuntimeException("Moto não encontrada com a placa: " + placa);
        }
        
        // Buscar o ponto de destino pelo nome
        List<PontosLeitura> pontosComNome = pontosLeituraRepository.findByNomePonto(dto.nomePontoDestino());
        if (pontosComNome.isEmpty()) {
            System.out.println("DEBUG: Ponto não encontrado: " + dto.nomePontoDestino());
            throw new RuntimeException("Ponto '" + dto.nomePontoDestino() + "' não encontrado");
        }
        
        // Se houver múltiplos pontos com o mesmo nome, pegar o primeiro
        PontosLeitura pontoDestino = pontosComNome.get(0);
        
        System.out.println("DEBUG: Moto encontrada - ID: " + moto.getIdMoto());
        System.out.println("DEBUG: Ponto destino: " + pontoDestino.getNomePonto() + " (ID: " + pontoDestino.getIdPonto() + ")");
        
        // Criar nova movimentação
        Movimentacoes movimentacao = new Movimentacoes();
        movimentacao.setMoto(moto);
        movimentacao.setPontoLeitura(pontoDestino);
        movimentacao.setDataHora(LocalDateTime.now());
        
        movimentacoesRepository.save(movimentacao);
        System.out.println("DEBUG: Movimentação registrada");
        
        // Retornar moto com novo ponto
        String novoPonto = pontoDestino.getNomePonto();
        System.out.println("DEBUG: Novo ponto: " + novoPonto);
        
        MotoComPontoAtualDTO resultado = new MotoComPontoAtualDTO(moto.getIdMoto(), moto.getModelo(), moto.getPlaca(), novoPonto);
        System.out.println("DEBUG: Resultado final - ponto: " + resultado.ponto());
        
        return resultado;
    }
}