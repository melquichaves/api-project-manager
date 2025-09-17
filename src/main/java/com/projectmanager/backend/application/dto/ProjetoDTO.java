package com.projectmanager.backend.application.dto;

import com.projectmanager.backend.domain.model.StatusProjeto;
import java.time.LocalDate;

public class ProjetoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataPrevFim;
    private LocalDate dataRealFim;
    private StatusProjeto status;
    private UsuarioDTO responsavel; // Reutilizamos o UsuarioDTO que já temos

    // Construtores, Getters e Setters
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

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataPrevFim() {
        return dataPrevFim;
    }

    public void setDataPrevFim(LocalDate dataPrevFim) {
        this.dataPrevFim = dataPrevFim;
    }

    public LocalDate getDataRealFim() {
        return dataRealFim;
    }

    public void setDataRealFim(LocalDate dataRealFim) {
        this.dataRealFim = dataRealFim;
    }

    public StatusProjeto getStatus() {
        return status;
    }

    public void setStatus(StatusProjeto status) {
        this.status = status;
    }

    public UsuarioDTO getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UsuarioDTO responsavel) {
        this.responsavel = responsavel;
    }
}