/**
 * Author: dnj
 * Date: Mar 5, 2008, 4:43:20 PM
 * 6.005 Elements of Software Construction
 * (c) 2008, MIT and Daniel Jackson
 */
package sudoku.src.immutable;

/**
 * Implementation of immutable generic map using association list. See Map for
 * specification.
 */
public class ImListMap<K, V> implements ImMap<K, V> {
    /*
     * Rep invariant bindings != null bindings contains no null elements, keys
     * or values no duplicate keys
     */
    private ImList<Binding> bindings;

    void checkRep() {
        // check whether assertions are turned on.
        // if they're not on, we want to avoid the quadratic-cost
        // traversal that checkRep(bindings) would do.
        try {
            assert false;
        } catch (AssertionError e) {
            checkRep(bindings);
        }
    }

    void checkRep(ImList<Binding> bs) {
        if (!bs.isEmpty()) {
            Binding b = bs.first();
            assert b.key != null : "ListMap, Rep invariant: keys non-null";
            assert b.value != null : "ListMap, Rep invariant: values non-null";
            assert !new ImListMap<K, V>(bs.rest()).containsKey(b.key);
            checkRep(bs.rest());
        }
    }

    private class Binding {
        K key;
        V value;

        Binding(K k, V v) {
            key = k;
            value = v;
        }

        public String toString() {
            return key.toString() + "->" + value.toString();
        }
    }

    public ImListMap() {
        this.bindings = new EmptyImList<Binding>();
        checkRep();
    }

    // Internal constructor.
    private ImListMap(ImList<Binding> bindings) {
        this.bindings = bindings;
        // don't call checkRep() here, because this constructor is used by
        // checkRep!
    }

    public boolean containsKey(Object k) {
        return get(bindings, k) != null;
    }

    public V get(Object k) {
        Binding b = get(bindings, k);
        if (b == null)
            return null;
        else
            return b.value;
    }

    /*
     * search through list recursively to find binding with matching key
     */
    private Binding get(ImList<Binding> bindings, Object key) {
        if (bindings.size() == 0)
            return null;
        Binding b = bindings.first();
        if (b.key.equals(key))
            return b;
        else
            return get(bindings.rest(), key);
    }

    public ImMap<K, V> put(K key, V value) {
        return new ImListMap<K, V>(put(bindings, key, value));
    }

    /*
     * recursively construct new list with binding for given key replaced or
     * added
     */
    private ImList<Binding> put(ImList<Binding> bindings, K key, V value) {
        if (bindings.size() == 0)
            return new NonEmptyImList<Binding>(new Binding(key, value));
        Binding b = bindings.first();
        if (b.key.equals(key)) {
            b = new Binding(key, value);
            return bindings.rest().add(b);
        } else {
            return put(bindings.rest(), key, value).add(b);
        }
    }

    public int size() {
        return bindings.size();
    }

    public boolean isEmpty() {
        return bindings.size() == 0;
    }

    public String toString() {
        return bindings.toString();
    }
}