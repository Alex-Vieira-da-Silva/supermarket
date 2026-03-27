package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.exception.ClienteNaoEncontradoException;
import com.accenture.supermarket.exception.DuplicateResourceException;
import com.accenture.supermarket.mapper.ClienteMapper;
import com.accenture.supermarket.model.Cliente;
import com.accenture.supermarket.repository.ClienteRepository;
import com.accenture.supermarket.util.CpfUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public Page<Cliente> listar(String nome, String cpf, Pageable pageable) {
        if (StringUtils.hasText(cpf)) {
            return repository.findByCpfContaining(cpf, pageable);
        }
        if (StringUtils.hasText(nome)) {
            return repository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return repository.findAll(pageable);
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    public Cliente criar(ClienteDTO dto) {
        String formattedCpf = CpfUtils.format(dto.getCpf());
        if (StringUtils.hasText(formattedCpf) && repository.existsByCpf(formattedCpf)) {
            throw new DuplicateResourceException("CPF ja cadastrado");
        }
        Cliente cliente = ClienteMapper.toEntity(dto);
        return repository.save(cliente);
    }

    public Cliente atualizar(Long id, ClienteDTO dto) {
        Cliente cliente = buscarPorId(id);
        String formattedCpf = CpfUtils.format(dto.getCpf());
        if (StringUtils.hasText(formattedCpf) && repository.existsByCpfAndIdNot(formattedCpf, id)) {
            throw new DuplicateResourceException("CPF ja cadastrado");
        }
        ClienteMapper.updateEntity(cliente, dto);
        return repository.save(cliente);
    }

    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);
        repository.delete(cliente);
    }

}
