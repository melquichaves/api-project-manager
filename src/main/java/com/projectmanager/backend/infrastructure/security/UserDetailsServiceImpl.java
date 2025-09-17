package com.projectmanager.backend.infrastructure.security;

import com.projectmanager.backend.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // No nosso sistema, o 'username' é o 'login'
        return usuarioRepository.findByLogin(username)
                .map(usuario -> new User(
                        usuario.getLogin(),
                        usuario.getSenha(),
                        // Converte nosso enum de perfil para uma GrantedAuthority do Spring Security
                        Collections.singletonList(() -> "ROLE_" + usuario.getPerfil().name())))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o login: " + username));
    }
}