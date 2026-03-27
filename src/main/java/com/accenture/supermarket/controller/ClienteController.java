package com.accenture.supermarket.controller;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.dto.response.ClienteResponse;
import com.accenture.supermarket.dto.response.PageResponse;
import com.accenture.supermarket.service.ClienteService;
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

@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna clientes com paginação, filtro por nome ou CPF")
    public ResponseEntity<PageResponse<ClienteResponse>> listar(
            @Parameter(description = "Filtro parcial pelo nome do cliente") @RequestParam(required = false) String nome,
            @Parameter(description = "Filtro parcial pelo CPF") @RequestParam(required = false) String cpf,
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        PageResponse<ClienteResponse> resposta = PageResponse.from(
                service.listar(nome, cpf, pageable).map(ClienteResponse::from)
        );

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente", description = "Busca um cliente pelo identificador")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteResponse.from(service.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Criar cliente", description = "Cria um novo cliente")
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteDTO dto) {
        var cliente = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/clientes/" + cliente.getId()))
                .body(ClienteResponse.from(cliente));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Atualizar cliente", description = "Atualiza um cliente existente")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(ClienteResponse.from(service.atualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover cliente", description = "Remove um cliente existente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
