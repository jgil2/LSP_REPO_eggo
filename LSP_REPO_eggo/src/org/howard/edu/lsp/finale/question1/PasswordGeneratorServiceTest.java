package org.howard.edu.lsp.finale.question1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for PasswordGeneratorService.
 */
public class PasswordGeneratorServiceTest {

    private PasswordGeneratorService service;

    @BeforeEach
    public void setup() {
        // getInstance resets internal algorithm selection to null (see documentation)
        service = PasswordGeneratorService.getInstance();
    }

    @Test
    public void checkInstanceNotNull() {
        assertNotNull(service, "getInstance() should not return null");
    }

    @Test
    public void checkSingleInstanceBehavior() {
        PasswordGeneratorService second = PasswordGeneratorService.getInstance();
        // Both references must point to the exact same object
        assertSame(service, second, "getInstance() must always return the same instance");
    }

    @Test
    public void generateWithoutSettingAlgorithmThrowsException() {
        PasswordGeneratorService s = PasswordGeneratorService.getInstance();
        // No algorithm selected (reset in getInstance), so this must throw
        assertThrows(IllegalStateException.class, () -> {
            s.generatePassword(8);
        }, "generatePassword should throw IllegalStateException when algorithm is not set");
    }

    @Test
    public void basicAlgorithmGeneratesCorrectLengthAndDigitsOnly() {
        service.setAlgorithm("basic");
        String p = service.generatePassword(10);
        assertNotNull(p);
        assertEquals(10, p.length(), "Basic algorithm should produce requested length");
        assertTrue(p.matches("[0-9]+"), "Basic algorithm should produce digits only");
    }

    @Test
    public void enhancedAlgorithmGeneratesCorrectCharactersAndLength() {
        service.setAlgorithm("enhanced");
        String p = service.generatePassword(12);
        assertNotNull(p);
        assertEquals(12, p.length(), "Enhanced algorithm should produce requested length");
        assertTrue(p.matches("[A-Za-z0-9]+"), "Enhanced algorithm should produce letters and digits only");
    }

    @Test
    public void lettersAlgorithmGeneratesLettersOnly() {
        service.setAlgorithm("letters");
        String p = service.generatePassword(8);
        assertNotNull(p);
        assertEquals(8, p.length(), "Letters algorithm should produce requested length");
        assertTrue(p.matches("[A-Za-z]+"), "Letters algorithm should produce letters only");
    }

    @Test
    public void switchingAlgorithmsChangesBehavior() {
        service.setAlgorithm("basic");
        String p1 = service.generatePassword(10);

        service.setAlgorithm("letters");
        String p2 = service.generatePassword(10);

        service.setAlgorithm("enhanced");
        String p3 = service.generatePassword(10);

        assertNotNull(p1);
        assertNotNull(p2);
        assertNotNull(p3);

        assertEquals(10, p1.length());
        assertEquals(10, p2.length());
        assertEquals(10, p3.length());

        assertTrue(p1.matches("[0-9]+"), "When basic selected, password should be digits only");
        assertTrue(p2.matches("[A-Za-z]+"), "When letters selected, password should be letters only");
        assertTrue(p3.matches("[A-Za-z0-9]+"), "When enhanced selected, password should be alphanumeric");
    }
}
