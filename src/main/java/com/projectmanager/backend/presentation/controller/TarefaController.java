package com.projectmanager.backend.presentation.controller;

import com.projectmanager.backend.application.dto.TarefaCadastroDTO;
import com.projectmanager.backend.application.dto.TarefaDTO;
import com.projectmanager.backend.application.dto.TarefaUpdateDTO;
import com.projectmanager.backend.application.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaDTO> criarTarefa(@RequestBody @Valid TarefaCadastroDTO dto) {
        TarefaDTO novaTarefa = tarefaService.criarTarefa(dto);
        return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
    }

    // Endpoint para iniciar uma tarefa
    @PostMapping("/{id}/iniciar")
    public ResponseEntity<TarefaDTO> iniciarTarefa(@PathVariable Long id) {
        TarefaDTO tarefaAtualizada = tarefaService.iniciarTarefa(id);
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @PostMapping("/{id}/concluir")
    public ResponseEntity<TarefaDTO> concluirTarefa(@PathVariable Long id) {
        TarefaDTO tarefaAtualizada = tarefaService.concluirTarefa(id);
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<TarefaDTO> cancelarTarefa(@PathVariable Long id) {
        TarefaDTO tarefaAtualizada = tarefaService.cancelarTarefa(id);
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO> atualizarTarefa(@PathVariable Long id, @RequestBody @Valid TarefaUpdateDTO dto) {
        TarefaDTO tarefaAtualizada = tarefaService.atualizarTarefa(id, dto);
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }
}