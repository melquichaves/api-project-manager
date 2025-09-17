package com.projectmanager.backend.application.service;

import com.projectmanager.backend.application.dto.TarefaCadastroDTO;
import com.projectmanager.backend.application.dto.TarefaDTO;
import com.projectmanager.backend.application.dto.TarefaUpdateDTO;
import com.projectmanager.backend.application.dto.UsuarioDTO;
import com.projectmanager.backend.domain.model.*;
import com.projectmanager.backend.domain.repository.ProjetoRepository;
import com.projectmanager.backend.domain.repository.TarefaRepository;
import com.projectmanager.backend.domain.repository.UsuarioRepository;
import com.projectmanager.backend.infrastructure.exception.RecursoNaoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private ProjetoRepository projetoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public TarefaDTO criarTarefa(TarefaCadastroDTO dto) {
        Projeto projeto = projetoRepository.findById(dto.getProjetoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto", dto.getProjetoId()));

        Usuario responsavel = null;
        if (dto.getResponsavelId() != null) {
            responsavel = usuarioRepository.findById(dto.getResponsavelId())
                    .orElseThrow(
                            () -> new RecursoNaoEncontradoException("Usuário responsável", dto.getResponsavelId()));
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setDataInicioPrevista(dto.getDataInicioPrevista());
        tarefa.setDataFimPrevista(dto.getDataFimPrevista());
        tarefa.setProjeto(projeto);
        tarefa.setResponsavel(responsavel);
        tarefa.setStatus(StatusTarefa.PENDENTE); // Toda tarefa começa PENDENTE

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        return convertToDto(tarefaSalva);
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> listarTarefasPorProjeto(Long projetoId) {
        if (!projetoRepository.existsById(projetoId)) {
            throw new RecursoNaoEncontradoException("Projeto", projetoId);
        }
        return tarefaRepository.findByProjetoId(projetoId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Métodos para mudar o status
    @Transactional
    public TarefaDTO iniciarTarefa(Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa", tarefaId));
        tarefa.iniciar();
        return convertToDto(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaDTO concluirTarefa(Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa", tarefaId));
        tarefa.concluir(); // Usa o método de domínio
        return convertToDto(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaDTO cancelarTarefa(Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa", tarefaId));
        tarefa.cancelar(); // Usa o método de domínio
        return convertToDto(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaDTO atualizarTarefa(Long tarefaId, TarefaUpdateDTO dto) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa", tarefaId));

        // Atualiza os campos básicos
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setDataInicioPrevista(dto.getDataInicioPrevista());
        tarefa.setDataFimPrevista(dto.getDataFimPrevista());

        // Atualiza o responsável
        if (dto.getResponsavelId() != null) {
            Usuario responsavel = usuarioRepository.findById(dto.getResponsavelId())
                    .orElseThrow(
                            () -> new RecursoNaoEncontradoException("Usuário responsável", dto.getResponsavelId()));
            tarefa.setResponsavel(responsavel);
        } else {
            tarefa.setResponsavel(null); // Permite remover o responsável
        }

        Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
        return convertToDto(tarefaAtualizada);
    }

    @Transactional
    public void deletarTarefa(Long tarefaId) {
        if (!tarefaRepository.existsById(tarefaId)) {
            throw new RecursoNaoEncontradoException("Tarefa", tarefaId);
        }
        tarefaRepository.deleteById(tarefaId);
    }

    private TarefaDTO convertToDto(Tarefa tarefa) {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(tarefa.getId());
        dto.setTitulo(tarefa.getTitulo());
        dto.setDescricao(tarefa.getDescricao());
        dto.setStatus(tarefa.getStatus());
        dto.setDataInicioPrevista(tarefa.getDataInicioPrevista());
        dto.setDataFimPrevista(tarefa.getDataFimPrevista());
        dto.setDataInicioReal(tarefa.getDataInicioReal());
        dto.setDataFimReal(tarefa.getDataFimReal());
        dto.setProjetoId(tarefa.getProjeto().getId());

        if (tarefa.getResponsavel() != null) {
            Usuario responsavel = tarefa.getResponsavel();
            dto.setResponsavel(new UsuarioDTO(
                    responsavel.getId(),
                    responsavel.getNomeCompleto(),
                    responsavel.getCpf(),
                    responsavel.getEmail(),
                    responsavel.getCargo(),
                    responsavel.getLogin(),
                    responsavel.getPerfil()));
        }
        return dto;
    }
}