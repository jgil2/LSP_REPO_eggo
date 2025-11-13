package org.howard.edu.lsp.assignment6;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;

public class IntegerSetTest {

    @Test
    public void testClearAndIsEmpty() {
        IntegerSet set = new IntegerSet();
        set.add(1);
        set.add(2);
        set.clear();
        assertTrue(set.isEmpty(), "Set should be empty after clear()");
    }

    @Test
    public void testLength() {
        IntegerSet set = new IntegerSet();
        set.add(1);
        set.add(2);
        assertEquals(2, set.length());
        set.remove(1);
        assertEquals(1, set.length());
    }

    @Test
    public void testEquals() {
        IntegerSet set1 = new IntegerSet();
        IntegerSet set2 = new IntegerSet();
        set1.add(1); set1.add(2);
        set2.add(2); set2.add(1);
        assertTrue(set1.equals(set2), "Sets with same elements should be equal");
        set2.add(3);
        assertFalse(set1.equals(set2), "Sets with different elements should not be equal");
    }

    @Test
    public void testContains() {
        IntegerSet set = new IntegerSet();
        set.add(10);
        assertTrue(set.contains(10));
        assertFalse(set.contains(5));
    }

    @Test
    public void testLargestAndSmallest() {
        IntegerSet set = new IntegerSet();
        set.add(5);
        set.add(10);
        set.add(1);
        assertEquals(10, set.largest());
        assertEquals(1, set.smallest());

        // test exception for empty set
        IntegerSet emptySet = new IntegerSet();
        assertThrows(IllegalStateException.class, () -> emptySet.largest());
        assertThrows(IllegalStateException.class, () -> emptySet.smallest());
    }

    @Test
    public void testAddDoesNotDuplicate() {
        IntegerSet set = new IntegerSet();
        set.add(3);
        set.add(3);
        assertEquals(1, set.length(), "Duplicate values should not be added");
    }

    @Test
    public void testRemove() {
        IntegerSet set = new IntegerSet();
        set.add(1);
        set.add(2);
        set.remove(1);
        assertFalse(set.contains(1));
        set.remove(3);  // removing nonexistent value should not throw
        assertEquals(1, set.length());
    }

    @Test
    public void testUnion() {
        IntegerSet set1 = new IntegerSet();
        set1.add(1); set1.add(2);
        IntegerSet set2 = new IntegerSet();
        set2.add(2); set2.add(3);
        set1.union(set2);
        assertEquals("[1, 2, 3]", set1.toString());
    }

    @Test
    public void testIntersect() {
        IntegerSet set1 = new IntegerSet();
        set1.add(1); set1.add(2); set1.add(3);
        IntegerSet set2 = new IntegerSet();
        set2.add(2); set2.add(3); set2.add(4);
        set1.intersect(set2);
        assertEquals("[2, 3]", set1.toString());
    }

    @Test
    public void testDiff() {
        IntegerSet set1 = new IntegerSet();
        set1.add(1); set1.add(2); set1.add(3);
        IntegerSet set2 = new IntegerSet();
        set2.add(2);
        set1.diff(set2);
        assertEquals("[1, 3]", set1.toString());
    }

    @Test
    public void testComplement() {
        IntegerSet set1 = new IntegerSet();
        set1.add(1); set1.add(2);
        IntegerSet set2 = new IntegerSet();
        set2.add(2); set2.add(3); set2.add(4);
        set1.complement(set2);
        assertEquals("[3, 4]", set1.toString());
    }

    @Test
    public void testIsEmpty() {
        IntegerSet set = new IntegerSet();
        assertTrue(set.isEmpty());
        set.add(1);
        assertFalse(set.isEmpty());
    }

    @Test
    public void testToStringFormat() {
        IntegerSet set = new IntegerSet();
        set.add(10);
        set.add(20);
        String output = set.toString();
        assertTrue(output.startsWith("[") && output.endsWith("]"));
    }
}
