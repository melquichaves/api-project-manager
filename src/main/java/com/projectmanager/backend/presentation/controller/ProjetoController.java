package com.projectmanager.backend.presentation.controller;

import com.projectmanager.backend.application.dto.ProjetoCadastroDTO;
import com.projectmanager.backend.application.dto.ProjetoDTO;
import com.projectmanager.backend.application.dto.ProjetoUpdateDTO;
import com.projectmanager.backend.application.service.ProjetoService;
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
}