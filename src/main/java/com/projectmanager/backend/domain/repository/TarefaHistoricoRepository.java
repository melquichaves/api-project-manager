package com.projectmanager.backend.domain.repository;

import com.projectmanager.backend.domain.model.TarefaHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaHistoricoRepository extends JpaRepository<TarefaHistorico, Long> {
    List<TarefaHistorico> findByTarefaIdOrderByDataModificacaoDesc(Long tarefaId);
}