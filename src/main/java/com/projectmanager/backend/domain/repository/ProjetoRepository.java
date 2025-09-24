package com.projectmanager.backend.domain.repository;

import com.projectmanager.backend.domain.model.Projeto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    @EntityGraph(attributePaths = {"equipes", "equipes.membros"})
    List<Projeto> findAll();

    @EntityGraph(attributePaths = {"equipes", "equipes.membros"})
    Optional<Projeto> findById(Long id);
}