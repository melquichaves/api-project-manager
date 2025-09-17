package com.projectmanager.backend.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class EquipeCadastroDTO {
    @NotBlank(message = "O nome da equipe é obrigatório")
    private String nome;
    private String descricao;
    private List<Long> membroIds; // Para o cadastro, recebemos apenas os IDs dos usuários

    // Getters e Setters
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

    public List<Long> getMembroIds() {
        return membroIds;
    }

    public void setMembroIds(List<Long> membroIds) {
        this.membroIds = membroIds;
    }
}