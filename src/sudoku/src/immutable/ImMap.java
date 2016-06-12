/**
 * Author: dnj
 * Date: Mar 5, 2008, 4:22:46 PM
 * 6.005 Elements of Software Construction
 * (c) 2008, MIT and Daniel Jackson
 */
package sudoku.src.immutable;

/**
 * Interface for immutable generic map from keys to values.
 * Keys may not be null.
 */
public interface ImMap <K,V>{

    /**
     * Tests to see if key is bound. 
     * @return true iff the key k has a value associated with it in this map
     */
    boolean containsKey (Object k);

    /**
     * Looks up value associated with key.
     * @return value associated with key k in this map, or null if k is not bound
     */
    V get (Object k);
    
    /**
     * Adds binding from key to value to this map. Requires that key is non-null.
     * @return new map obtained by binding k to v in this, replacing any existing binding for k
     */
    ImMap<K,V> put (K key, V value);

    /**
     * Returns size of map. 
     * @return number of keys bound in this map
     */
    int size ();

    /**
     * Emptiness check.
     * @return true iff this map binds no keys
     */
    boolean isEmpty ();    
}