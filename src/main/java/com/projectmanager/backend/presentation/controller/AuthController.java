package com.projectmanager.backend.presentation.controller;

import com.projectmanager.backend.application.dto.LoginRequestDTO;
import com.projectmanager.backend.application.dto.LoginResponseDTO;
import com.projectmanager.backend.application.dto.UsuarioDTO;
import com.projectmanager.backend.domain.model.Usuario;
import com.projectmanager.backend.domain.repository.UsuarioRepository;
import com.projectmanager.backend.infrastructure.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getSenha()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);

        Usuario usuario = usuarioRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getId(), usuario.getNomeCompleto(), usuario.getCpf(), usuario.getEmail(), usuario.getCargo(), usuario.getLogin(), usuario.getPerfil());

        return ResponseEntity.ok(new LoginResponseDTO(jwtToken, usuarioDTO));
    }
}