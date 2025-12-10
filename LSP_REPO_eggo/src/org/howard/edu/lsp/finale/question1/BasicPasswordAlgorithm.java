package org.howard.edu.lsp.finale.question1;

import java.util.Random;

/**
 * Basic password algorithm: uses java.util.Random and produces digits only (0-9).
 */
public class BasicPasswordAlgorithm implements PasswordAlgorithm {
    private static final String DIGITS = "0123456789";
    private final Random random;

    /**
     * Construct a BasicPasswordAlgorithm with a new Random instance.
     */
    public BasicPasswordAlgorithm() {
        this.random = new Random();
    }

    /**
     * Generate a digits-only password using java.util.Random.
     *
     * @param length length of password to generate
     * @return digits-only password string
     */
    @Override
    public String generate(int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(DIGITS.length());
            sb.append(DIGITS.charAt(idx));
        }
        return sb.toString();
    }
}
