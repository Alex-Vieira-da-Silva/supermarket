package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.exception.ClienteNaoEncontradoException;
import com.accenture.supermarket.model.Cliente;
import com.accenture.supermarket.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(
                        "Cliente não encontrado com ID: " + id));
    }

    public Cliente criar(ClienteDTO dto) {
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .build();

        return repository.save(cliente);
    }

    public Cliente atualizar(Long id, ClienteDTO dto) {
        Cliente cliente = buscarPorId(id);
        cliente.atualizar(dto);
        return repository.save(cliente);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ClienteNaoEncontradoException(
                    "Cliente não encontrado com ID: " + id);
        }

        repository.deleteById(id);
    }
}
