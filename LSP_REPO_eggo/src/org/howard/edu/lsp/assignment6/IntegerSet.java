package org.howard.edu.lsp.assignment6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The IntegerSet class models a mathematical set of integers using an ArrayList.
 * Duplicates are not allowed, and all standard set operations are supported.
 * 
 * @author Jada Gilfillian
 * @version 1.0
 */
public class IntegerSet  {
  private List<Integer> set = new ArrayList<Integer>();

  // Clears the internal representation of the set.
  public void clear() { 
      set.clear();
  }

/**
* Returns the number of elements in the set.
* 
* @return the size of the set
*/
  public int length() { 
      return set.size();
  }

  /**
   * Returns true if the 2 sets are equal, false otherwise;
   * Two sets are equal if they contain all of the same values in ANY order.
   * This overrides the equals method from the Object class.
   * 
   * @param o the object to compare to this set
   * @return true if both sets contain the same elements; false otherwise
   */
  @Override
  public boolean equals(Object o) { 
      if (this == o) return true;
      if (!(o instanceof IntegerSet)) return false;
      IntegerSet other = (IntegerSet) o;
      return set.containsAll(other.set) && other.set.containsAll(set);
  }

  /**
   * Returns true if the set contains the value, otherwise false.
   * 
   * @param value the integer value to check
   * @return true if the value is in the set; false otherwise
   */ 
  public boolean contains(int value) { 
      return set.contains(value);
  }

  /**
   * Returns the largest item in the set (throws IllegalStateException if empty).
   * @return the largest integer in the set
   * @throws IllegalStateException if the set is empty
   */
  public int largest()  { 
      if (set.isEmpty()) {
          throw new IllegalStateException("Set is empty");
      }
      return Collections.max(set);
  }

  /**
   * Returns the smallest element in the set (throws IllegalStateException if empty).
   * 
   * @return the smallest integer in the set
   * @throws IllegalStateException if the set is empty
   */
  public int smallest()  { 
      if (set.isEmpty()) {
          throw new IllegalStateException("Set is empty");
      }
      return Collections.min(set);
  }

  /** Adds an item to the set or does nothing if already present.
   * 
   * @param item the integer to add
   */
  public void add(int item) { 
      if (!set.contains(item)) {
          set.add(item);
      }
  }

  /**
   * Removes an item from the set or does nothing if not there.
   * 
   * @param item the integer to remove
   */
  public void remove(int item) { 
      set.remove(Integer.valueOf(item));
  }

  /**
   * Set union: modifies this to contain all unique elements in this or other.
   * 
   * @param other the other IntegerSet to union with
   */
  public void union(IntegerSet other) { 
      for (int value : other.set) {
          if (!set.contains(value)) {
              set.add(value);
          }
      }
  }

  /**
   * Set intersection: modifies this to contain only elements in both sets.
   * 
   * @param other the other IntegerSet to intersect with
   */
  public void intersect(IntegerSet other) { 
      set.retainAll(other.set);
  }

  /**
   * Set difference (this \ other): modifies this to remove elements found in other.
   * 
   * @param other the other IntegerSet to compare with
   */
  public void diff(IntegerSet other) { 
      set.removeAll(other.set);
  }

  /** 
   * Set complement: modifies this to become (other \ this).
   * 
   * @param other the other IntegerSet to complement with
   */
  public void complement(IntegerSet other) { 
      List<Integer> temp = new ArrayList<>(other.set);
      temp.removeAll(set);
      set = temp;
  }

  /**
   * Returns true if the set is empty, false otherwise.
   * 
   * @return true if the set contains no elements; false otherwise
   */
  public boolean isEmpty() { 
      return set.isEmpty();
  }

  /**
   * Returns a String representation; overrides Object.toString().
   * 
   * @return a string representing the set in list format
   */
  @Override
  public String toString() { 
      return set.toString();
  }
}
