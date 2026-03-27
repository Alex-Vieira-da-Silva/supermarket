package com.accenture.supermarket.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneUtilsTest {

    @Test
    void shouldFormatPhoneWithElevenDigits() {
        assertThat(PhoneUtils.format("81999999999")).isEqualTo("(81)99999-9999");
    }

    @Test
    void shouldReturnTrimmedPhoneWhenLengthIsInvalid() {
        assertThat(PhoneUtils.format("1234")).isEqualTo("1234");
    }

    @Test
    void shouldHandleNullPhone() {
        assertThat(PhoneUtils.format(null)).isNull();
    }
}
