package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.exception.ProdutoNaoEncontradoException;
import com.accenture.supermarket.model.Produto;
import com.accenture.supermarket.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com ID: " + id));
    }

    public Produto criar(ProdutoDTO dto) {
        Produto produto = Produto.builder()
                .nome(dto.getNome())
                .preco(dto.getPreco())
                .quantidade(dto.getQuantidade())
                .build();

        return repository.save(produto);
    }

    public Produto atualizar(Long id, ProdutoDTO dto) {
        Produto produto = buscarPorId(id);

        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setQuantidade(dto.getQuantidade());

        return repository.save(produto);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}