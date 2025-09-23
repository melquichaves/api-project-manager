package com.projectmanager.backend.application.service;

import com.projectmanager.backend.application.dto.TarefaCadastroDTO;
import com.projectmanager.backend.application.dto.TarefaDTO;
import com.projectmanager.backend.application.dto.TarefaHistoricoDTO;
import com.projectmanager.backend.application.dto.TarefaUpdateDTO;
import com.projectmanager.backend.application.dto.UsuarioDTO;
import com.projectmanager.backend.domain.model.*;
import com.projectmanager.backend.domain.repository.ProjetoRepository;
import com.projectmanager.backend.domain.repository.TarefaHistoricoRepository;
import com.projectmanager.backend.domain.repository.TarefaRepository;
import com.projectmanager.backend.domain.repository.UsuarioRepository;
import com.projectmanager.backend.infrastructure.exception.RecursoNaoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private TarefaHistoricoRepository tarefaHistoricoRepository;

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

        registrarHistorico(tarefaSalva, "Tarefa criada.");

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

        registrarHistorico(tarefa, "Status alterado para EM_ANDAMENTO.");

        return convertToDto(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaDTO concluirTarefa(Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa", tarefaId));
        tarefa.concluir();

        registrarHistorico(tarefa, "Status alterado para CONCLUIDA.");

        return convertToDto(tarefaRepository.save(tarefa));
    }

        @Transactional
    public TarefaDTO cancelarTarefa(Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa", tarefaId));
        tarefa.cancelar();

        registrarHistorico(tarefa, "Status alterado para CANCELADA.");

        return convertToDto(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaDTO reabrirTarefa(Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa", tarefaId));
        tarefa.reabrir(); // Chama o método de domínio para reabrir a tarefa

        registrarHistorico(tarefa, "Status alterado para PENDENTE (Tarefa reaberta).");

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

        registrarHistorico(tarefaAtualizada, "Dados da tarefa foram atualizados.");

        return convertToDto(tarefaAtualizada);
    }

    @Transactional
    public void deletarTarefa(Long tarefaId) {
        if (!tarefaRepository.existsById(tarefaId)) {
            throw new RecursoNaoEncontradoException("Tarefa", tarefaId);
        }
        tarefaRepository.deleteById(tarefaId);
    }

    @Transactional(readOnly = true)
    public List<TarefaHistoricoDTO> listarHistoricoPorTarefa(Long tarefaId) {
        if (!tarefaRepository.existsById(tarefaId)) {
            throw new RecursoNaoEncontradoException("Tarefa", tarefaId);
        }
        List<TarefaHistorico> historico = tarefaHistoricoRepository.findByTarefaIdOrderByDataModificacaoDesc(tarefaId);
        return historico.stream()
                .map(this::convertHistoricoToDto)
                .toList();
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

    // HELPER PARA REGISTRAR O HISTÓRICO
    private void registrarHistorico(Tarefa tarefa, String descricao) {
        // Pega o login do usuário atualmente autenticado
        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        // Busca o usuário no banco de dados
        Usuario usuarioModificacao = usuarioRepository.findByLogin(loginUsuario)
                .orElse(null); // Pode ser nulo se a ação for do sistema

        TarefaHistorico historico = new TarefaHistorico(tarefa, descricao, usuarioModificacao);
        tarefaHistoricoRepository.save(historico);
    }

    // MÉTODO HELPER PARA CONVERTER HISTÓRICO PARA DTO
    private TarefaHistoricoDTO convertHistoricoToDto(TarefaHistorico historico) {
        TarefaHistoricoDTO dto = new TarefaHistoricoDTO();
        dto.setId(historico.getId());
        dto.setDataModificacao(historico.getDataModificacao());
        dto.setDescricao(historico.getDescricao());
        if (historico.getUsuarioModificacao() != null) {
            dto.setUsuarioModificacaoNome(historico.getUsuarioModificacao().getNomeCompleto());
        } else {
            dto.setUsuarioModificacaoNome("Sistema");
        }
        return dto;
    }
}