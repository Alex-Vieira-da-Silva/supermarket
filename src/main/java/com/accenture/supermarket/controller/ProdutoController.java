package com.accenture.supermarket.controller;

import com.accenture.supermarket.dto.response.PageResponse;
import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.dto.response.ProdutoResponse;
import com.accenture.supermarket.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna produtos com paginação e filtro opcional por nome")
    public ResponseEntity<PageResponse<ProdutoResponse>> listarTodos(
            @Parameter(description = "Filtro parcial pelo nome do produto") @RequestParam(required = false) String nome,
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        PageResponse<ProdutoResponse> resposta = PageResponse.from(
                service.listar(nome, pageable).map(ProdutoResponse::from)
        );
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto", description = "Busca um produto pelo identificador")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ProdutoResponse.from(service.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Criar produto", description = "Cria um novo produto")
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoDTO dto) {
        var produto = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/produtos/" + produto.getId()))
                .body(ProdutoResponse.from(produto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ProdutoDTO dto) {
        return ResponseEntity.ok(ProdutoResponse.from(service.atualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Remover produto", description = "Remove um produto existente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
