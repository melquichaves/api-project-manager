package com.projectmanager.backend.domain.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "equipes")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    // Relacionamento Muitos-para-Muitos com Usuario
        @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "equipe_membros", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "equipe_id"), // Coluna que se refere a Equipe
            inverseJoinColumns = @JoinColumn(name = "usuario_id") // Coluna que se refere a Usuario
    )
    private Set<Usuario> membros = new HashSet<>(); // Usar Set é uma boa prática para evitar duplicatas

    // Relacionamento Muitos-para-Muitos com Projeto (será o lado inverso)
    @JsonBackReference
    @ManyToMany(mappedBy = "equipes")
    private Set<Projeto> projetos = new HashSet<>();

    public Equipe() {
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

    public Set<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(Set<Usuario> membros) {
        this.membros = membros;
    }

    public Set<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(Set<Projeto> projetos) {
        this.projetos = projetos;
    }

    // --- Métodos de domínio para gerenciar membros ---
    public void adicionarMembro(Usuario usuario) {
        this.membros.add(usuario);
    }

    public void removerMembro(Usuario usuario) {
        this.membros.remove(usuario);
    }

    // --- equals e hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Equipe equipe = (Equipe) o;
        return Objects.equals(id, equipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}