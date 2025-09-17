package com.projectmanager.backend.presentation.controller;

import com.projectmanager.backend.application.dto.EquipeCadastroDTO;
import com.projectmanager.backend.application.dto.EquipeDTO;
import com.projectmanager.backend.application.dto.EquipeUpdateDTO;
import com.projectmanager.backend.application.service.EquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @PostMapping
    public ResponseEntity<EquipeDTO> criarEquipe(@RequestBody @Valid EquipeCadastroDTO dto) {
        EquipeDTO novaEquipe = equipeService.criarEquipe(dto);
        return new ResponseEntity<>(novaEquipe, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EquipeDTO>> listarTodasEquipes() {
        List<EquipeDTO> equipes = equipeService.listarTodas();
        return ResponseEntity.ok(equipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<EquipeDTO>> buscarEquipePorId() {
        List<EquipeDTO> equipes = equipeService.listarTodas();
        return ResponseEntity.ok(equipes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeDTO> atualizarEquipe(@PathVariable Long id, @RequestBody @Valid EquipeUpdateDTO dto) {
        EquipeDTO equipeAtualizada = equipeService.atualizarEquipe(id, dto);
        return ResponseEntity.ok(equipeAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEquipe(@PathVariable Long id) {
        equipeService.deletarEquipe(id);
        return ResponseEntity.noContent().build();
    }
}