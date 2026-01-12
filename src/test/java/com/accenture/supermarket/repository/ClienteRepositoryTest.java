package com.accenture.supermarket.repository;

import com.accenture.supermarket.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

    @Test
    void deveSalvarCliente() {
        Cliente cliente = new Cliente(null, "Alex", "12345678901", "81999999999", "alex@email");

        Cliente salvo = repository.save(cliente);

        assertNotNull(salvo.getId());
        assertEquals("Alex", salvo.getNome());
    }

    @Test
    void deveBuscarPorId() {
        Cliente cliente = new Cliente(null, "Alex", "12345678901", "81999999999", "alex@email");
        Cliente salvo = repository.save(cliente);

        Cliente encontrado = repository.findById(salvo.getId()).orElse(null);

        assertNotNull(encontrado);
        assertEquals("Alex", encontrado.getNome());
    }
}
