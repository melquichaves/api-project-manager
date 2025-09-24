package com.projectmanager.backend.presentation.controller;

import com.projectmanager.backend.application.dto.ProjetoCadastroDTO;
import com.projectmanager.backend.application.dto.ProjetoDTO;
import com.projectmanager.backend.application.dto.ProjetoUpdateDTO;
import com.projectmanager.backend.application.dto.TarefaDTO;
import com.projectmanager.backend.application.service.ProjetoService;
import com.projectmanager.backend.application.service.TarefaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<ProjetoDTO> criarProjeto(@RequestBody @Valid ProjetoCadastroDTO dto) {
        ProjetoDTO novoProjeto = projetoService.criarProjeto(dto);
        return new ResponseEntity<>(novoProjeto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjetoDTO>> listarTodosProjetos() {
        List<ProjetoDTO> projetos = projetoService.listarTodos();
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoDTO> buscarProjetoPorId(@PathVariable Long id) {
        ProjetoDTO projeto = projetoService.buscarPorId(id);
        return ResponseEntity.ok(projeto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoDTO> atualizarProjeto(@PathVariable Long id,
            @RequestBody @Valid ProjetoUpdateDTO dto) {
        ProjetoDTO projetoAtualizado = projetoService.atualizarProjeto(id, dto);
        return ResponseEntity.ok(projetoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProjeto(@PathVariable Long id) {
        projetoService.deletarProjeto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projetoId}/equipes/{equipeId}")
    public ResponseEntity<ProjetoDTO> adicionarEquipe(@PathVariable Long projetoId, @PathVariable Long equipeId) {
        ProjetoDTO projetoAtualizado = projetoService.adicionarEquipeAoProjeto(projetoId, equipeId);
        return ResponseEntity.ok(projetoAtualizado);
    }

    @DeleteMapping("/{projetoId}/equipes/{equipeId}")
    public ResponseEntity<Void> removerEquipe(@PathVariable Long projetoId, @PathVariable Long equipeId) {
        projetoService.removerEquipeDoProjeto(projetoId, equipeId);
        return ResponseEntity.noContent().build();
    }

        @GetMapping("/{projetoId}/tarefas")
    public ResponseEntity<List<TarefaDTO>> listarTarefasDoProjeto(@PathVariable Long projetoId) {
        List<TarefaDTO> tarefas = tarefaService.listarTarefasPorProjeto(projetoId);
        return ResponseEntity.ok(tarefas);
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<ProjetoDTO> iniciarProjeto(@PathVariable Long id) {
        ProjetoDTO projetoAtualizado = projetoService.iniciarProjeto(id);
        return ResponseEntity.ok(projetoAtualizado);
    }

    @PostMapping("/{id}/concluir")
    public ResponseEntity<ProjetoDTO> concluirProjeto(@PathVariable Long id) {
        ProjetoDTO projetoAtualizado = projetoService.concluirProjeto(id);
        return ResponseEntity.ok(projetoAtualizado);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<ProjetoDTO> cancelarProjeto(@PathVariable Long id) {
        ProjetoDTO projetoAtualizado = projetoService.cancelarProjeto(id);
        return ResponseEntity.ok(projetoAtualizado);
    }

    @PutMapping("/{id}/reabrir")
    public ResponseEntity<ProjetoDTO> reabrirProjeto(@PathVariable Long id) {
        ProjetoDTO projetoAtualizado = projetoService.reabrirProjeto(id);
        return ResponseEntity.ok(projetoAtualizado);
    }
}