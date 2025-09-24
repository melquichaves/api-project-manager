package com.projectmanager.backend.domain.repository;

import com.projectmanager.backend.domain.model.Equipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
    @EntityGraph(attributePaths = "projetos")
    Optional<Equipe> findById(Long id);
}