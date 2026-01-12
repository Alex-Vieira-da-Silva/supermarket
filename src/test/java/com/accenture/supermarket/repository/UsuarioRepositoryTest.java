package com.accenture.supermarket.repository;

import com.accenture.supermarket.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    void deveSalvarUsuario() {
        Usuario usuario = new Usuario(null, "alex", "123", "ADMIN");

        Usuario salvo = repository.save(usuario);

        assertNotNull(salvo.getId());
        assertEquals("alex", salvo.getUsername());
    }

    @Test
    void deveBuscarPorId() {
        Usuario usuario = new Usuario(null, "alex", "123", "ADMIN");
        Usuario salvo = repository.save(usuario);

        Usuario encontrado = repository.findById(salvo.getId()).orElse(null);

        assertNotNull(encontrado);
        assertEquals("alex", encontrado.getUsername());
    }
}
