package com.projectmanager.backend.domain.repository;

import com.projectmanager.backend.domain.model.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
    // Métodos customizados
}