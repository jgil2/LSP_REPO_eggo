package org.howard.edu.lsp.finale.question1;

import java.security.SecureRandom;

/**
 * Enhanced password algorithm: uses SecureRandom and produces characters from
 * A-Z, a-z, 0-9.
 */
public class EnhancedPasswordAlgorithm implements PasswordAlgorithm {
    private static final String ALLOWED =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "abcdefghijklmnopqrstuvwxyz" +
        "0123456789";

    private final SecureRandom secureRandom;

    /**
     * Construct an EnhancedPasswordAlgorithm using SecureRandom.
     */
    public EnhancedPasswordAlgorithm() {
        this.secureRandom = new SecureRandom();
    }

    /**
     * Generate an enhanced alphanumeric password using SecureRandom.
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
            int idx = secureRandom.nextInt(ALLOWED.length());
            sb.append(ALLOWED.charAt(idx));
        }
        return sb.toString();
    }
}
