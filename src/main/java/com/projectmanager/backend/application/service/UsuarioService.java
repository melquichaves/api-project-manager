package com.projectmanager.backend.application.service;

import com.projectmanager.backend.application.dto.UsuarioCadastroDTO;
import com.projectmanager.backend.application.dto.UsuarioDTO;
import com.projectmanager.backend.domain.model.Tarefa;
import com.projectmanager.backend.domain.model.Usuario;
import com.projectmanager.backend.domain.repository.TarefaRepository;
import com.projectmanager.backend.domain.repository.UsuarioRepository;
import com.projectmanager.backend.infrastructure.exception.ConflitoDeDadosException;
import com.projectmanager.backend.infrastructure.exception.RecursoNaoEncontradoException;
import com.projectmanager.backend.utils.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioDTO getCurrentUser() {
        // Obter o usuário autenticado do contexto de segurança
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Usuario usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário logado não encontrado", 0L));
        return convertToDto(usuario);
    }

    public UsuarioDTO cadastrarUsuario(UsuarioCadastroDTO dto) {
        if (usuarioRepository.existsByLogin(dto.getLogin())) {
            throw new ConflitoDeDadosException("Login '" + dto.getLogin() + "' já existe.");
        }
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new ConflitoDeDadosException("CPF '" + dto.getCpf() + "' já existe.");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ConflitoDeDadosException("E-mail '" + dto.getEmail() + "' já existe.");
        }

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setCargo(dto.getCargo());
        usuario.setLogin(dto.getLogin());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setPerfil(dto.getPerfil());

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDto(savedUsuario);
    }

    public List<UsuarioDTO> listarTodosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public UsuarioDTO buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id)); // Lança nossa exceção customizada
        return convertToDto(usuario);
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioCadastroDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));

        if (!Validators.isCpfValido(dto.getCpf())) {
            throw new ConflitoDeDadosException("CPF '" + dto.getCpf() + "' é inválido.");
        }

        // Validar unicidade para campos que não podem ser repetidos
        if (!usuario.getLogin().equals(dto.getLogin()) && usuarioRepository.existsByLogin(dto.getLogin())) {
            throw new ConflitoDeDadosException("Login '" + dto.getLogin() + "' já existe.");
        }
        if (!usuario.getCpf().equals(dto.getCpf()) && usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new ConflitoDeDadosException("CPF '" + dto.getCpf() + "' já existe.");
        }
        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ConflitoDeDadosException("E-mail '" + dto.getEmail() + "' já existe.");
        }

        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setCargo(dto.getCargo());
        usuario.setLogin(dto.getLogin());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setPerfil(dto.getPerfil());

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return convertToDto(updatedUsuario);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        // Verifica se o usuário existe
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));

        // Encontra todas as tarefas onde este usuário é o responsável
        List<Tarefa> tarefasDoUsuario = tarefaRepository.findByResponsavelId(id);

        // Remove o responsável das tarefas
        for (Tarefa tarefa : tarefasDoUsuario) {
            tarefa.setResponsavel(null);
            tarefaRepository.save(tarefa);
        }

        // Agora pode deletar o usuário
        usuarioRepository.delete(usuario);
    }

    private UsuarioDTO convertToDto(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getCargo(),
                usuario.getLogin(),
                usuario.getPerfil());
    }
}