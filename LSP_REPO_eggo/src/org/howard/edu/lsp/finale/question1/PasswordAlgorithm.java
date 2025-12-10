package org.howard.edu.lsp.finale.question1;

/**
 * Strategy interface for password generation algorithms.
 * Implementations produce a password string of a requested length.
 */
public interface PasswordAlgorithm {
    /**
     * Generate a password of the requested length.
     *
     * @param length desired password length (assumed >= 0)
     * @return generated password
     */
    String generate(int length);
}
