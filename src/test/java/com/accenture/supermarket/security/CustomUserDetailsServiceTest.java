package com.accenture.supermarket.security;

import com.accenture.supermarket.model.Usuario;
import com.accenture.supermarket.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldLoadUserWithRolePrefix() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .username("alex")
                .password("encoded")
                .role("ADMIN")
                .build();

        when(usuarioRepository.findByUsername("alex")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("alex");

        assertThat(userDetails.getUsername()).isEqualTo("alex");
        assertThat(userDetails.getPassword()).isEqualTo("encoded");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(usuarioRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("missing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("missing");
    }
}
