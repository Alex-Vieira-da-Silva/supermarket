package com.accenture.supermarket.config;

import com.accenture.supermarket.model.Usuario;
import com.accenture.supermarket.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner ensureDefaultAdmin(
            @Value("${app.admin.username:admin}") String username,
            @Value("${app.admin.password:Super@123}") String password,
            @Value("${app.admin.role:ADMIN}") String role
    ) {
        return args -> usuarioRepository.findByUsername(username).ifPresentOrElse(
                user -> log.debug("Usuario padrao ja existe: {}", user.getUsername()),
                () -> {
                    Usuario admin = Usuario.builder()
                            .username(username)
                            .password(passwordEncoder.encode(password))
                            .role(role)
                            .build();

                    usuarioRepository.save(admin);
                    log.info("Usuario padrao '{}' criado com role '{}'", username, role);
                }
        );
    }
}
