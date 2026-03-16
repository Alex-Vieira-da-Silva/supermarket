package com.accenture.supermarket.repository;

import com.accenture.supermarket.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    @DisplayName("Deve salvar um usuário corretamente")
    void deveSalvarUsuario() {
        Usuario usuario = new Usuario(null, "alex", "123", "ADMIN");

        Usuario salvo = repository.save(usuario);

        assertAll(
                () -> assertNotNull(salvo.getId()),
                () -> assertEquals("alex", salvo.getUsername()),
                () -> assertEquals("123", salvo.getPassword()),
                () -> assertEquals("ADMIN", salvo.getRole())
        );
    }

    @Test
    @DisplayName("Deve buscar um usuário pelo ID")
    void deveBuscarPorId() {
        Usuario usuario = new Usuario(null, "alex", "123", "ADMIN");
        Usuario salvo = repository.save(usuario);

        Optional<Usuario> opt = repository.findById(salvo.getId());

        assertTrue(opt.isPresent());
        Usuario encontrado = opt.get();

        assertAll(
                () -> assertEquals("alex", encontrado.getUsername()),
                () -> assertEquals("123", encontrado.getPassword()),
                () -> assertEquals("ADMIN", encontrado.getRole())
        );
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar ID inexistente")
    void deveRetornarVazioQuandoIdNaoExiste() {
        Optional<Usuario> resultado = repository.findById(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar um usuário pelo ID")
    void deveDeletarUsuario() {
        Usuario usuario = new Usuario(null, "alex", "123", "ADMIN");
        Usuario salvo = repository.save(usuario);

        repository.deleteById(salvo.getId());

        Optional<Usuario> resultado = repository.findById(salvo.getId());

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar um usuário existente")
    void deveAtualizarUsuario() {
        Usuario usuario = new Usuario(null, "alex", "123", "ADMIN");
        Usuario salvo = repository.save(usuario);

        salvo.setUsername("novo");
        salvo.setRole("USER");

        Usuario atualizado = repository.save(salvo);

        assertAll(
                () -> assertEquals("novo", atualizado.getUsername()),
                () -> assertEquals("USER", atualizado.getRole())
        );
    }

    @Test
    @DisplayName("Deve retornar true quando existsById encontrar o usuário")
    void deveVerificarExistenciaPorId() {
        Usuario usuario = new Usuario(null, "alex", "123", "ADMIN");
        Usuario salvo = repository.save(usuario);

        assertTrue(repository.existsById(salvo.getId()));
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void deveListarTodos() {
        repository.save(new Usuario(null, "alex", "123", "ADMIN"));

        List<Usuario> lista = repository.findAll();

        assertFalse(lista.isEmpty());
    }
}