package com.accenture.supermarket.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CpfUtilsTest {

    @Test
    void shouldFormatCpfWithElevenDigits() {
        assertThat(CpfUtils.format("12345678901")).isEqualTo("123.456.789-01");
    }

    @Test
    void shouldReturnTrimmedCpfWhenLengthIsInvalid() {
        assertThat(CpfUtils.format("123")).isEqualTo("123");
    }

    @Test
    void shouldHandleNullCpf() {
        assertThat(CpfUtils.format(null)).isNull();
    }
}
