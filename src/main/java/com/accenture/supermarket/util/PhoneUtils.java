package com.accenture.supermarket.util;

import org.springframework.util.StringUtils;

/**
 * Utilidades para normalizar e formatar números de telefone.
 */
public final class PhoneUtils {

    private PhoneUtils() {
    }

    /**
     * Remove caracteres não numéricos de um telefone.
     */
    public static String digitsOnly(String telefone) {
        return telefone == null ? null : telefone.replaceAll("\\D", "");
    }

    /**
     * Formata o telefone para o padrão (DD)00000-0000 quando possuir 11 dígitos.
     * Retorna o valor original (trimado) caso não tenha o tamanho esperado.
     */
    public static String format(String telefone) {
        if (!StringUtils.hasText(telefone)) {
            return telefone;
        }

        String digits = digitsOnly(telefone);
        if (digits.length() != 11) {
            return telefone.trim();
        }

        return String.format("(%s)%s-%s",
                digits.substring(0, 2),
                digits.substring(2, 7),
                digits.substring(7));
    }
}
