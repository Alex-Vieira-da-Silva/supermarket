package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.exception.ClienteNaoEncontradoException;
import com.accenture.supermarket.exception.DuplicateResourceException;
import com.accenture.supermarket.model.Cliente;
import com.accenture.supermarket.repository.ClienteRepository;
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
        String formattedCpf = formatCpf(dto.getCpf());
        if (StringUtils.hasText(formattedCpf) && repository.existsByCpf(formattedCpf)) {
            throw new DuplicateResourceException("CPF ja cadastrado");
        }
        String formattedTelefone = formatTelefone(dto.getTelefone());
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .cpf(formattedCpf)
                .telefone(formattedTelefone)
                .email(dto.getEmail())
                .build();

        return repository.save(cliente);
    }

    public Cliente atualizar(Long id, ClienteDTO dto) {
        Cliente cliente = buscarPorId(id);
        String formattedCpf = formatCpf(dto.getCpf());
        String formattedTelefone = formatTelefone(dto.getTelefone());
        if (StringUtils.hasText(formattedCpf) && repository.existsByCpfAndIdNot(formattedCpf, id)) {
            throw new DuplicateResourceException("CPF ja cadastrado");
        }
        cliente.setNome(dto.getNome());
        cliente.setCpf(formattedCpf);
        cliente.setTelefone(formattedTelefone);
        cliente.setEmail(dto.getEmail());
        return repository.save(cliente);
    }

    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);
        repository.delete(cliente);
    }

    private String formatCpf(String cpf) {
        if (!StringUtils.hasText(cpf)) {
            return cpf;
        }
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) {
            return cpf.trim();
        }
        return String.format("%s.%s.%s-%s",
                digits.substring(0, 3),
                digits.substring(3, 6),
                digits.substring(6, 9),
                digits.substring(9));
    }

    private String formatTelefone(String telefone) {
        if (!StringUtils.hasText(telefone)) {
            return telefone;
        }
        String digits = telefone.replaceAll("\\D", "");
        if (digits.length() != 11) {
            return telefone.trim();
        }
        return String.format("(%s)%s-%s",
                digits.substring(0, 2),
                digits.substring(2, 7),
                digits.substring(7));
    }
}
