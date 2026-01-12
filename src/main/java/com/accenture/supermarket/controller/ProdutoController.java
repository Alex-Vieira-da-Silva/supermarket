package com.accenture.supermarket.controller;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.service.ProdutoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping
    public List<ProdutoDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ProdutoDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ProdutoDTO criar(@Valid @RequestBody ProdutoDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public ProdutoDTO atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
