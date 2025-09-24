package com.projectmanager.backend.application.service;

import com.projectmanager.backend.application.dto.EquipeCadastroDTO;
import com.projectmanager.backend.application.dto.EquipeDTO;
import com.projectmanager.backend.application.dto.EquipeUpdateDTO;
import com.projectmanager.backend.application.dto.ProjetoDTO;
import com.projectmanager.backend.application.dto.UsuarioDTO;
import com.projectmanager.backend.domain.model.Equipe;
import com.projectmanager.backend.domain.model.Projeto;
import com.projectmanager.backend.domain.model.Usuario;
import com.projectmanager.backend.domain.repository.EquipeRepository;
import com.projectmanager.backend.domain.repository.UsuarioRepository;
import com.projectmanager.backend.infrastructure.exception.RecursoNaoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public EquipeDTO criarEquipe(EquipeCadastroDTO dto) {
        Equipe equipe = new Equipe();
        equipe.setNome(dto.getNome());
        equipe.setDescricao(dto.getDescricao());

        if (dto.getMembroIds() != null && !dto.getMembroIds().isEmpty()) {
            Set<Usuario> membros = new HashSet<>(usuarioRepository.findAllById(dto.getMembroIds()));
            if (membros.size() != dto.getMembroIds().size()) {
                throw new RecursoNaoEncontradoException("Um ou mais usuários membros não foram encontrados.");
            }
            equipe.setMembros(membros);
        }

        Equipe equipeSalva = equipeRepository.save(equipe);
        return convertToDto(equipeSalva);
    }

    @Transactional(readOnly = true)
    public List<EquipeDTO> listarTodas() {
        return equipeRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public EquipeDTO buscarPorId(Long id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe", id));
        return convertToDto(equipe);
    }

    @Transactional
    public EquipeDTO atualizarEquipe(Long id, EquipeUpdateDTO dto) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe", id));

        equipe.setNome(dto.getNome());
        equipe.setDescricao(dto.getDescricao());

        // Atualiza a lista de membros
        equipe.getMembros().clear(); // Limpa a lista atual de membros
        if (dto.getMembroIds() != null && !dto.getMembroIds().isEmpty()) {
            Set<Usuario> novosMembros = new HashSet<>(usuarioRepository.findAllById(dto.getMembroIds()));
            equipe.setMembros(novosMembros);
        }

        Equipe equipeAtualizada = equipeRepository.save(equipe);
        return convertToDto(equipeAtualizada);
    }

    @Transactional
    public void deletarEquipe(Long id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe", id));

        // Remove a equipe de todos os projetos associados
        equipe.getProjetos().forEach(projeto -> projeto.removerEquipe(equipe));

        equipeRepository.delete(equipe);
    }

    private EquipeDTO convertToDto(Equipe equipe) {
        EquipeDTO dto = new EquipeDTO();
        dto.setId(equipe.getId());
        dto.setNome(equipe.getNome());
        dto.setDescricao(equipe.getDescricao());

        List<UsuarioDTO> membrosDto = equipe.getMembros().stream()
                .map(membro -> new UsuarioDTO(
                        membro.getId(),
                        membro.getNomeCompleto(),
                        membro.getCpf(),
                        membro.getEmail(),
                        membro.getCargo(),
                        membro.getLogin(),
                        membro.getPerfil()))
                .toList();
        dto.setMembros(membrosDto);

        return dto;
    }
}