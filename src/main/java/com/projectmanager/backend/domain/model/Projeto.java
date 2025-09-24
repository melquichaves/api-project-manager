package com.projectmanager.backend.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate; // Usando LocalDate, que é mais moderno que java.util.Date
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "projetos")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataPrevFim;

    @Column // Data real do fim é opcional, só preenchida ao concluir
    private LocalDate dataRealFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProjeto status;

    @ManyToOne(fetch = FetchType.LAZY) // Um projeto tem um responsável
    @JoinColumn(name = "responsavel_id", nullable = true)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.SET_NULL)
    private Usuario responsavel;

    // NOVO RELACIONAMENTO: Lado "dono" da relação Projeto-Equipe
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "projeto_equipes", joinColumns = @JoinColumn(name = "projeto_id"), inverseJoinColumns = @JoinColumn(name = "equipe_id"))
    private Set<Equipe> equipes = new HashSet<>();

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tarefa> tarefas = new HashSet<>();

    public Projeto() {
        // Construtor Padrão
    }

    // --- Getters e Setters ---
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

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }

    public Set<Equipe> getEquipes() {
        return equipes;
    }

    public void setEquipes(Set<Equipe> equipes) {
        this.equipes = equipes;
    }

    // --- Métodos de domínio para gerenciar equipes ---
    public void adicionarEquipe(Equipe equipe) {
        this.equipes.add(equipe);
        equipe.getProjetos().add(this);
    }

    public void removerEquipe(Equipe equipe) {
        this.equipes.remove(equipe);
        equipe.getProjetos().remove(this);
    }

    // --- Métodos de Domínio para Status do Projeto ---
    public void iniciar() {
        if (this.status == StatusProjeto.PLANEJADO) {
            this.status = StatusProjeto.EM_ANDAMENTO;
        } else {
            throw new IllegalStateException("O projeto não pode ser iniciado pois não está planejado.");
        }
    }

    public void concluir() {
        if (this.status == StatusProjeto.EM_ANDAMENTO) {
            this.status = StatusProjeto.CONCLUIDO;
            this.dataRealFim = LocalDate.now();
        } else {
            throw new IllegalStateException("O projeto não pode ser concluído pois não está em andamento.");
        }
    }

    public void cancelar() {
        if (this.status == StatusProjeto.PLANEJADO || this.status == StatusProjeto.EM_ANDAMENTO) {
            this.status = StatusProjeto.CANCELADO;
        } else {
            throw new IllegalStateException("O projeto não pode ser cancelado pois já está concluído ou cancelado.");
        }
    }

    public void reabrir() {
        if (this.status == StatusProjeto.CANCELADO || this.status == StatusProjeto.CONCLUIDO) {
            this.status = StatusProjeto.PLANEJADO;
            this.dataRealFim = null; // Limpa a data de fim real ao reabrir
        } else {
            throw new IllegalStateException("O projeto não pode ser reaberto pois não está cancelado ou concluído.");
        }
    }

    // --- equals e hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Projeto projeto = (Projeto) o;
        return Objects.equals(id, projeto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}