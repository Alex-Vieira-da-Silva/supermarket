package com.accenture.supermarket.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private static final String SECRET_32_BYTES = "01234567890123456789012345678901";

    @Test
    void generateTokenShouldReturnValidToken() {
        JwtTokenProvider provider = new JwtTokenProvider(SECRET_32_BYTES, 60_000);

        String token = provider.generateToken("alex");

        assertThat(provider.isValid(token)).isTrue();
        assertThat(provider.isExpired(token)).isFalse();
        assertThat(provider.getUsername(token)).isEqualTo("alex");
    }

    @Test
    void expiredTokenShouldBeInvalid() throws InterruptedException {
        JwtTokenProvider provider = new JwtTokenProvider(SECRET_32_BYTES, 5);

        String token = provider.generateToken("alex");
        Thread.sleep(10);

        assertThat(provider.isValid(token)).isFalse();
        assertThat(provider.isExpired(token)).isTrue();
    }

    @Test
    void invalidSignatureShouldBeRejected() {
        JwtTokenProvider provider = new JwtTokenProvider(SECRET_32_BYTES, 60_000);
        JwtTokenProvider otherProvider = new JwtTokenProvider("99999999999999999999999999999999", 60_000);

        String token = provider.generateToken("alex");

        assertThat(otherProvider.isValid(token)).isFalse();
    }
}
