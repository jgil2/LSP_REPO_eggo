package org.howard.edu.lsp.assignment6;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegerSetTest {

    @Test
    public void testAddAndContains() {
        IntegerSet s = new IntegerSet();
        s.add(1); s.add(2);
        assertTrue(s.contains(1));
        assertEquals(2, s.length());
        s.add(2); // duplicate ignored
        assertEquals(2, s.length());
    }

    @Test
    public void testRemove() {
        IntegerSet s = new IntegerSet();
        s.add(1); s.remove(1);
        assertTrue(s.isEmpty());
    }

    @Test
    public void testLargestSmallest() {
        IntegerSet s = new IntegerSet();
        s.add(5); s.add(2); s.add(9);
        assertEquals(9, s.largest());
        assertEquals(2, s.smallest());
        assertThrows(IllegalStateException.class, () -> new IntegerSet().largest());
    }

    @Test
    public void testUnion() {
        IntegerSet a = new IntegerSet(); a.add(1); a.add(2);
        IntegerSet b = new IntegerSet(); b.add(2); b.add(3);
        a.union(b);
        assertTrue(a.contains(3));
        assertEquals(3, a.length());
    }

    @Test
    public void testIntersect() {
        IntegerSet a = new IntegerSet(); a.add(1); a.add(2);
        IntegerSet b = new IntegerSet(); b.add(2); b.add(3);
        a.intersect(b);
        assertEquals("[2]", a.toString());
    }

    @Test
    public void testDiffAndComplement() {
        IntegerSet a = new IntegerSet(); a.add(1); a.add(2);
        IntegerSet b = new IntegerSet(); b.add(2); b.add(3);
        a.diff(b);
        assertEquals("[1]", a.toString());
        a.complement(b);
        assertEquals("[2, 3]", a.toString());
    }

    @Test
    public void testEqualsAndToString() {
        IntegerSet a = new IntegerSet(); a.add(1); a.add(2);
        IntegerSet b = new IntegerSet(); b.add(2); b.add(1);
        assertTrue(a.equals(b));
        assertEquals("[1, 2]", a.toString());
    }

    @Test
    public void testClearAndIsEmpty() {
        IntegerSet s = new IntegerSet();
        s.add(5); s.clear();
        assertTrue(s.isEmpty());
    }
}