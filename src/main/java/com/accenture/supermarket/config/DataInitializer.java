package com.accenture.supermarket.config;

import com.accenture.supermarket.model.Usuario;
import com.accenture.supermarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Cria um usuário admin padrão na inicialização (apenas fora do perfil de teste).
 */
@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner init(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            repo.findByUsername("admin").ifPresentOrElse(
                    existing -> { /* já existe, nada a fazer */ },
                    () -> {
                        Usuario admin = new Usuario();
                        admin.setUsername("admin");
                        admin.setPassword(encoder.encode("Super@123"));
                        admin.setRole("ADMIN");
                        repo.save(admin);
                        System.out.println("Usuário admin criado com sucesso!");
                    }
            );
        };
    }
}
