package es.bytescolab.ms_accounts.utils.math;

import java.math.BigInteger;
import java.util.Random;

public class IbanGenerator {

    private static final String COUNTRY_CODE = "ES";
    private static final int BBAN_LENGTH = 20;
    private static final int IBAN_LENGTH = BBAN_LENGTH + 4; // ES + CC + BBAN

    private static final BigInteger NINETY_SEVEN = BigInteger.valueOf(97); // Para ejecutar el algoritmo Mod 97
    private static final Random RANDOM = new Random();

    private IbanGenerator() {
    }


    // Construye el IBAN completo: Codigo de país + dígitos de control + BBAN
    public static String generate() {
        String bban = generateBban();
        String checkDigits = computeCheckDigits(bban);
        return COUNTRY_CODE + checkDigits + bban;
    }

    // Valida un IBAN usando el algoritmo MOD-97.
    public static boolean isValid(String iban) {
        if (iban == null || iban.length() != IBAN_LENGTH) return false;
        if (!iban.startsWith(COUNTRY_CODE)) return false;

        // BBAN + país y dígitos de control
        String rearranged = iban.substring(4) + iban.substring(0, 4);

        return lettersToDigits(rearranged)
                       .mod(NINETY_SEVEN)
                       .intValue() == 1;
    }

    // Genera 20 dígitos numéricos aleatorios (BBAN).
    private static String generateBban() {
        StringBuilder sb = new StringBuilder(BBAN_LENGTH);
        for (int i = 0; i < BBAN_LENGTH; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    // Calcula los 2 dígitos de control IBAN para el BBAN generado.
    private static String computeCheckDigits(String bban) {
        String rearranged = bban + COUNTRY_CODE + "00";
        int remainder = lettersToDigits(rearranged).mod(NINETY_SEVEN).intValue();
        int checkDigits = 98 - remainder;
        return String.format("%02d", checkDigits);
    }

    private static BigInteger lettersToDigits(String rearranged) {
        StringBuilder numericIban = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            numericIban.append(Character.isLetter(c) ? Character.getNumericValue(c) : c);
        }
        return new BigInteger(numericIban.toString());
    }
}