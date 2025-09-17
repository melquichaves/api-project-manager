package com.projectmanager.backend.application.dto;

import com.projectmanager.backend.domain.model.StatusProjeto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ProjetoUpdateDTO {

    @NotBlank(message = "O nome do projeto é obrigatório")
    private String nome;

    private String descricao;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "A data de término prevista é obrigatória")
    @FutureOrPresent(message = "A data de término prevista não pode ser no passado")
    private LocalDate dataPrevFim;

    private LocalDate dataRealFim; // Permitimos a atualização da data real de fim

    @NotNull(message = "O status é obrigatório")
    private StatusProjeto status; // Permitimos a atualização do status

    @NotNull(message = "O ID do responsável é obrigatório")
    private Long responsavelId;

    // --- Getters e Setters ---
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

    public Long getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }
}