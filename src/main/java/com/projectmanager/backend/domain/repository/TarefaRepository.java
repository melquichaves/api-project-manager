package com.projectmanager.backend.domain.repository;

import com.projectmanager.backend.domain.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // Método para buscar todas as tarefas associadas a um projeto
    List<Tarefa> findByProjetoId(Long projetoId);
}