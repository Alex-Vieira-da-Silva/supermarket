package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.exception.ProdutoNaoEncontradoException;
import com.accenture.supermarket.mapper.ProdutoMapper;
import com.accenture.supermarket.model.Produto;
import com.accenture.supermarket.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public Page<Produto> listar(String nome, Pageable pageable) {
        if (StringUtils.hasText(nome)) {
            return repository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return repository.findAll(pageable);
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    public Produto criar(ProdutoDTO dto) {
        Produto produto = ProdutoMapper.toEntity(dto);
        return repository.save(produto);
    }

    public Produto atualizar(Long id, ProdutoDTO dto) {
        Produto produto = buscarPorId(id);
        ProdutoMapper.updateEntity(produto, dto);
        return repository.save(produto);
    }

    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        repository.delete(produto);
    }
}
