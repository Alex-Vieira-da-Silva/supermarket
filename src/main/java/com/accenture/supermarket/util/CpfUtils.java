package com.accenture.supermarket.util;

import org.springframework.util.StringUtils;

/**
 * Utilidades para normalizar e formatar valores de CPF de maneira consistente em toda a aplicação.
 */
public final class CpfUtils {

    private CpfUtils() {
    }

    /**
     * Remove todos os caracteres não numéricos de um CPF.
     */
    public static String digitsOnly(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }

    /**
     * Formata o CPF para o padrão 000.000.000-00 quando possível.
     * Retorna o valor original (trimado) se não tiver 11 dígitos.
     */
    public static String format(String cpf) {
        if (!StringUtils.hasText(cpf)) {
            return cpf;
        }

        String digits = digitsOnly(cpf);
        if (digits.length() != 11) {
            return cpf.trim();
        }

        return String.format("%s.%s.%s-%s",
                digits.substring(0, 3),
                digits.substring(3, 6),
                digits.substring(6, 9),
                digits.substring(9));
    }
}
