package com.projectmanager.backend.application.service;

import com.projectmanager.backend.application.dto.ProjetoCadastroDTO;
import com.projectmanager.backend.application.dto.ProjetoDTO;
import com.projectmanager.backend.application.dto.ProjetoUpdateDTO;
import com.projectmanager.backend.application.dto.UsuarioDTO;
import com.projectmanager.backend.domain.model.Projeto;
import com.projectmanager.backend.domain.model.StatusProjeto;
import com.projectmanager.backend.domain.model.Usuario;
import com.projectmanager.backend.domain.repository.ProjetoRepository;
import com.projectmanager.backend.domain.repository.UsuarioRepository;
import com.projectmanager.backend.infrastructure.exception.RecursoNaoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public ProjetoDTO criarProjeto(ProjetoCadastroDTO dto) {
        Usuario responsavel = usuarioRepository.findById(dto.getResponsavelId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário responsável", dto.getResponsavelId()));

        Projeto projeto = new Projeto();
        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());
        projeto.setDataInicio(dto.getDataInicio());
        projeto.setDataPrevFim(dto.getDataPrevFim());
        projeto.setResponsavel(responsavel);
        projeto.setStatus(StatusProjeto.PLANEJADO);

        Projeto projetoSalvo = projetoRepository.save(projeto);
        return convertToDto(projetoSalvo);
    }

    @Transactional(readOnly = true)
    public ProjetoDTO buscarPorId(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto", id));
        return convertToDto(projeto);
    }

    @Transactional(readOnly = true)
    public List<ProjetoDTO> listarTodos() {
        return projetoRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public ProjetoDTO atualizarProjeto(Long id, ProjetoUpdateDTO dto) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto", id));

        Usuario responsavel = usuarioRepository.findById(dto.getResponsavelId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário responsável", dto.getResponsavelId()));

        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());
        projeto.setDataInicio(dto.getDataInicio());
        projeto.setDataPrevFim(dto.getDataPrevFim());
        projeto.setStatus(dto.getStatus());
        projeto.setResponsavel(responsavel);

        if (dto.getStatus() == StatusProjeto.CONCLUIDO && projeto.getDataRealFim() == null) {
            projeto.setDataRealFim(LocalDate.now());
        } else if (dto.getStatus() != StatusProjeto.CONCLUIDO) {
            projeto.setDataRealFim(null);
        } else {
            projeto.setDataRealFim(dto.getDataRealFim());
        }

        Projeto projetoAtualizado = projetoRepository.save(projeto);

        return convertToDto(projetoAtualizado);
    }

    @Transactional
    public void deletarProjeto(Long id) {
        if (!projetoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Projeto", id);
        }

        projetoRepository.deleteById(id);
    }

    // Método conversor de Entidade para DTO
    private ProjetoDTO convertToDto(Projeto projeto) {
        ProjetoDTO dto = new ProjetoDTO();
        dto.setId(projeto.getId());
        dto.setNome(projeto.getNome());
        dto.setDescricao(projeto.getDescricao());
        dto.setDataInicio(projeto.getDataInicio());
        dto.setDataPrevFim(projeto.getDataPrevFim());
        dto.setDataRealFim(projeto.getDataRealFim());
        dto.setStatus(projeto.getStatus());

        // Converte o Usuario aninhado para UsuarioDTO
        Usuario responsavel = projeto.getResponsavel();
        if (responsavel != null) {
            UsuarioDTO responsavelDto = new UsuarioDTO(
                    responsavel.getId(),
                    responsavel.getNomeCompleto(),
                    responsavel.getCpf(),
                    responsavel.getEmail(),
                    responsavel.getCargo(),
                    responsavel.getLogin(),
                    responsavel.getPerfil());
            dto.setResponsavel(responsavelDto);
        }

        return dto;
    }
}