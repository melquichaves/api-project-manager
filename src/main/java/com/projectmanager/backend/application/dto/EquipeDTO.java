package com.projectmanager.backend.application.dto;

import java.util.List;

public class EquipeDTO {
    private Long id;
    private String nome;
    private String descricao;
    private List<UsuarioDTO> membros;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<UsuarioDTO> getMembros() {
        return membros;
    }

    public void setMembros(List<UsuarioDTO> membros) {
        this.membros = membros;
    }
}