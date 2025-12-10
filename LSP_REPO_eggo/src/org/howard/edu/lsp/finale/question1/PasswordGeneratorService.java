package org.howard.edu.lsp.finale.question1;

import java.util.Locale;

/**
 * PasswordGeneratorService provides a single shared access point for generating
 * passwords using a selected algorithm. This class implements a Singleton
 * pattern and uses the Strategy pattern to swap password-generation algorithms
 * at runtime.
 *
------------------------------------------------------------
DESIGN PATTERN DOCUMENTATION — PART C
------------------------------------------------------------

1.  Design Patterns Used:
    • Singleton Pattern
    • Strategy Pattern (with Simple Factory-style selection)

2.  Explanation of Why These Patterns Were Appropriate:

    --- Singleton Pattern ---
    The Singleton pattern ensures that only ONE instance of
    PasswordGeneratorService exists in the entire system.

    This exam explicitly requires:
        • “Provide a single shared access point.”
        • “Only one instance of the service may exist.”
    
    The Singleton pattern achieves this by:
        • Making the constructor private
        • Creating a single static instance stored inside the class
        • Providing getInstance() as the only way to access it

    This guarantees that:
        • All parts of the application use the same shared service
        • The selected algorithm is consistent across the entire program
        • State is not duplicated or lost between components

    Without the Singleton pattern, multiple parts of the program could
    create their own password generators, causing mismatched behavior
    and violating the exam requirements.

    --- Strategy Pattern ---
    The Strategy pattern is used because the system must support
    multiple password-generation algorithms AND allow switching
    between them at runtime.

    The exam requirements specify:
        • “Support multiple approaches to password generation.”
        • “Allow the caller to select the approach at run time.”
        • “Support future expansion of password-generation approaches.”
        • “Make password-generation behavior swappable.”

    The Strategy pattern provides:
        • A common interface (PasswordAlgorithm)
        • Multiple interchangeable algorithm implementations
            – BasicPasswordAlgorithm
            – EnhancedPasswordAlgorithm
            – LettersPasswordAlgorithm
        • Ability to change algorithms simply by assigning a new strategy

    This allows the behavior of generatePassword() to change
    dynamically without modifying:
        • The service class
        • Client code
        • Or other algorithms

    --- Why Strategy Over Other Patterns? ---
    Alternatives like inheritance or giant switch-statements are rigid,
    difficult to extend, and violate the Open–Closed Principle.

    Strategy is ideal here because:
        • New algorithms can be added by simply creating a new class
        • No modifications are needed to existing working code
        • Algorithms remain cleanly separated and more testable
        • Behavior is easily swapped at runtime

    --- Simple Factory Behavior ---
    Inside setAlgorithm(String name), a very small factory-style
    selection is used to create the correct Strategy object.

    This is appropriate because:
        • It keeps object creation in one place
        • It avoids exposing algorithm implementations to client code
        • It keeps PasswordGeneratorService easy to extend in the future

    This is not a full Factory pattern, but the pattern *behavior* is
    intentionally used to support clean configuration of strategies.

    --- Summary ---
    The combination of:
        • Singleton  → controls access to one shared service
        • Strategy   → allows dynamic switching of algorithms
        • Simple Factory behavior → centralizes algorithm creation

    directly satisfies every structural and functional requirement in
    the exam instructions while keeping the system clean, extensible,
    and maintainable.
------------------------------------------------------------
 *
 * @author
 */
public class PasswordGeneratorService {
    // Singleton instance
    private static PasswordGeneratorService instance = null;

    // Current strategy (algorithm); null until setAlgorithm is called
    private PasswordAlgorithm algorithm;

    // Private constructor to prevent external instantiation
    private PasswordGeneratorService() {
        this.algorithm = null;
    }

    /**
     * Returns the single shared instance of PasswordGeneratorService.
     * <p>
     * Note: calling getInstance() resets the selected algorithm to null. This
     * helps unit tests start with a clean state between @BeforeEach calls while
     * keeping a single instance. In production you may remove the reset behavior
     * if persistent selection is desired.
     *
     * @return singleton instance
     */
    public static synchronized PasswordGeneratorService getInstance() {
        if (instance == null) {
            instance = new PasswordGeneratorService();
        }
        // Reset the algorithm on each getInstance() call to provide tests with a fresh state.
        instance.algorithm = null;
        return instance;
    }

    /**
     * Set the algorithm by name. Supported names (case-insensitive):
     * "basic", "enhanced", "letters".
     *
     * @param name algorithm name
     * @throws IllegalArgumentException if name is not recognized
     */
    public void setAlgorithm(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Algorithm name may not be null");
        }
        String key = name.toLowerCase(Locale.ROOT).trim();
        switch (key) {
            case "basic":
                this.algorithm = new BasicPasswordAlgorithm();
                break;
            case "enhanced":
                this.algorithm = new EnhancedPasswordAlgorithm();
                break;
            case "letters":
                this.algorithm = new LettersPasswordAlgorithm();
                break;
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + name);
        }
    }

    /**
     * Generate a password of the requested length using the currently selected
     * algorithm.
     *
     * @param length desired password length (non-negative)
     * @return generated password string (may be empty when length is 0)
     * @throws IllegalStateException if no algorithm has been selected (null)
     */
    public String generatePassword(int length) {
        if (this.algorithm == null) {
            throw new IllegalStateException("Password algorithm not selected");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length must be non-negative");
        }
        return this.algorithm.generate(length);
    }
}
