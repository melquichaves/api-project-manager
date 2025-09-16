package com.projectmanager.backend.utils;

public final class Validators {

    private Validators() {
    }

    public static boolean isCpfValido(String cpf) {
        if (cpf == null)
            return false;

        // remove tudo que não é número
        String num = cpf.replaceAll("\\D", "");
        if (num.length() != 11)
            return false;

        // bloqueia sequências repetidas (11111111111, 00000000000, ...)
        if (num.matches("(\\d)\\1{10}"))
            return false;

        // blacklist de CPFs "de teste" que passam pelo algoritmo mas são irreais
        if ("12345678909".equals(num))
            return false;

        try {
            // primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(num.charAt(i)) * (10 - i);
            }
            int resto = soma % 11;
            int dig1 = (resto < 2) ? 0 : 11 - resto;
            if (dig1 != Character.getNumericValue(num.charAt(9)))
                return false;

            // segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(num.charAt(i)) * (11 - i);
            }
            resto = soma % 11;
            int dig2 = (resto < 2) ? 0 : 11 - resto;
            return dig2 == Character.getNumericValue(num.charAt(10));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
