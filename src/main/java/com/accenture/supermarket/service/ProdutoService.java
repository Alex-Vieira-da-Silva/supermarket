package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Produto;
import com.accenture.supermarket.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public List<ProdutoDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(p -> new ProdutoDTO(p.getId(), p.getNome(), p.getPreco(), p.getQuantidade()))
                .toList();
    }

    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getPreco(), produto.getQuantidade());
    }

    public ProdutoDTO criar(ProdutoDTO dto) {
        Produto produto = new Produto(null, dto.getNome(), dto.getPreco(), dto.getQuantidade());
        Produto salvo = repository.save(produto);

        return new ProdutoDTO(salvo.getId(), salvo.getNome(), salvo.getPreco(), salvo.getQuantidade());
    }

    public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setQuantidade(dto.getQuantidade());

        Produto salvo = repository.save(produto);

        return new ProdutoDTO(salvo.getId(), salvo.getNome(), salvo.getPreco(), salvo.getQuantidade());
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Produto não encontrado");
        }
        repository.deleteById(id);
    }
}
