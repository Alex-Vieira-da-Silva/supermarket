package com.accenture.supermarket.repository;

import com.accenture.supermarket.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

    @Test
    @DisplayName("Deve salvar um cliente corretamente")
    void deveSalvarCliente() {
        Cliente cliente = new Cliente(null, "Alex", "12345678901", "81999999999", "alex@email");

        Cliente salvo = repository.save(cliente);

        assertAll(
                () -> assertNotNull(salvo.getId()),
                () -> assertEquals("Alex", salvo.getNome()),
                () -> assertEquals("123.456.789-01", salvo.getCpf()),
                () -> assertEquals("(81)99999-9999", salvo.getTelefone()),
                () -> assertEquals("alex@email", salvo.getEmail())
        );
    }

    @Test
    @DisplayName("Deve buscar cliente por ID")
    void deveBuscarPorId() {
        Cliente cliente = new Cliente(null, "Alex", "12345678901", "81999999999", "alex@email");
        Cliente salvo = repository.save(cliente);

        Optional<Cliente> encontrado = repository.findById(salvo.getId());

        assertAll(
                () -> assertTrue(encontrado.isPresent()),
                () -> assertEquals("Alex", encontrado.get().getNome()),
                () -> assertEquals("123.456.789-01", encontrado.get().getCpf()),
                () -> assertEquals("(81)99999-9999", encontrado.get().getTelefone())
        );
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar ID inexistente")
    void deveRetornarVazioQuandoIdNaoExiste() {
        Optional<Cliente> resultado = repository.findById(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar um cliente pelo ID")
    void deveDeletarCliente() {
        Cliente cliente = new Cliente(null, "Alex", "12345678901", "81999999999", "alex@email");
        Cliente salvo = repository.save(cliente);

        repository.deleteById(salvo.getId());

        Optional<Cliente> resultado = repository.findById(salvo.getId());

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar um cliente existente")
    void deveAtualizarCliente() {
        Cliente cliente = new Cliente(null, "Alex", "12345678901", "81999999999", "alex@email");
        Cliente salvo = repository.save(cliente);

        salvo.setNome("Novo Nome");
        Cliente atualizado = repository.save(salvo);

        assertEquals("Novo Nome", atualizado.getNome());
    }

    @Test
    @DisplayName("Deve retornar true quando existsById encontrar o cliente")
    void deveVerificarExistenciaPorId() {
        Cliente cliente = new Cliente(null, "Alex", "12345678901", "81999999999", "alex@email");
        Cliente salvo = repository.save(cliente);

        assertTrue(repository.existsById(salvo.getId()));
    }

    @Test
    @DisplayName("Deve listar todos os clientes")
    void deveListarTodos() {
        repository.save(new Cliente(null, "Alex", "12345678901", "81999999999", "alex@email"));

        List<Cliente> lista = repository.findAll();

        assertFalse(lista.isEmpty());
    }
}
