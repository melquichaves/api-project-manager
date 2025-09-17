package com.projectmanager.backend.application.dto;

import java.time.LocalDateTime;

public class TarefaHistoricoDTO {
    private Long id;
    private LocalDateTime dataModificacao;
    private String descricao;
    private String usuarioModificacaoNome; // Apenas o nome do usuário é suficiente

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(LocalDateTime dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUsuarioModificacaoNome() {
        return usuarioModificacaoNome;
    }

    public void setUsuarioModificacaoNome(String usuarioModificacaoNome) {
        this.usuarioModificacaoNome = usuarioModificacaoNome;
    }
}