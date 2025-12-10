package org.howard.edu.lsp.finale.question1;

import java.security.SecureRandom;

/**
 * Letters-only password algorithm: produces letters A-Z and a-z.
 */
public class LettersPasswordAlgorithm implements PasswordAlgorithm {
    private static final String LETTERS =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "abcdefghijklmnopqrstuvwxyz";

    private final SecureRandom secureRandom;

    /**
     * Construct a LettersPasswordAlgorithm using SecureRandom.
     */
    public LettersPasswordAlgorithm() {
        this.secureRandom = new SecureRandom();
    }

    /**
     * Generate a letters-only password.
     *
     * @param length password length
     * @return generated password string
     */
    @Override
    public String generate(int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = secureRandom.nextInt(LETTERS.length());
            sb.append(LETTERS.charAt(idx));
        }
        return sb.toString();
    }
}
