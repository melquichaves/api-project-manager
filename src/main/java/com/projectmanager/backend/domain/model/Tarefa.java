package com.projectmanager.backend.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status;

    private LocalDate dataInicioPrevista;
    private LocalDate dataFimPrevista;
    private LocalDate dataInicioReal;
    private LocalDate dataFimReal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    public Tarefa() {
        // Construtor padrão
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public LocalDate getDataInicioPrevista() {
        return dataInicioPrevista;
    }

    public void setDataInicioPrevista(LocalDate dataInicioPrevista) {
        this.dataInicioPrevista = dataInicioPrevista;
    }

    public LocalDate getDataFimPrevista() {
        return dataFimPrevista;
    }

    public void setDataFimPrevista(LocalDate dataFimPrevista) {
        this.dataFimPrevista = dataFimPrevista;
    }

    public LocalDate getDataInicioReal() {
        return dataInicioReal;
    }

    public void setDataInicioReal(LocalDate dataInicioReal) {
        this.dataInicioReal = dataInicioReal;
    }

    public LocalDate getDataFimReal() {
        return dataFimReal;
    }

    public void setDataFimReal(LocalDate dataFimReal) {
        this.dataFimReal = dataFimReal;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    // --- Métodos de Domínio ---
    public void iniciar() {
        if (this.status == StatusTarefa.PENDENTE) {
            this.status = StatusTarefa.EM_ANDAMENTO;
            this.dataInicioReal = LocalDate.now();
        } else {
            throw new IllegalStateException("A tarefa não pode ser iniciada pois não está pendente.");
        }
    }

    public void concluir() {
        if (this.status == StatusTarefa.EM_ANDAMENTO) {
            this.status = StatusTarefa.CONCLUIDA;
            this.dataFimReal = LocalDate.now();
        } else {
            throw new IllegalStateException("A tarefa não pode ser concluída pois não está em andamento.");
        }
    }

    public void cancelar() {
        this.status = StatusTarefa.CANCELADA;
    }

    public void reabrir() {
        if (this.status == StatusTarefa.CANCELADA || this.status == StatusTarefa.CONCLUIDA) {
            this.status = StatusTarefa.PENDENTE;
            this.dataInicioReal = null; // Limpa a data de início real ao reabrir
            this.dataFimReal = null;     // Limpa a data de fim real ao reabrir
        } else {
            throw new IllegalStateException("A tarefa não pode ser reaberta pois não está cancelada ou concluída.");
        }
    }

    // --- equals e hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tarefa tarefa = (Tarefa) o;
        return Objects.equals(id, tarefa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}