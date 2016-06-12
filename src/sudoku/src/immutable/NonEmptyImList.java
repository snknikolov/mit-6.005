/**
 * An immutable list
 * Designed for illustrating reasoning about immutable types
 * For more pragmatic design, see RCM version
 * 6.005 Elements of Software Construction, Fall 2007
 * Copyright 2007 Daniel Jackson
 */
package sudoku.src.immutable;

import java.util.Iterator;

public class NonEmptyImList<E> implements ImList<E> {
    private E element;
    private ImList<E> rest;
    private int size;

    /**
     * abstraction function
     * A(this) = <element> ^ A(rest)
     * 
     */

    void checkRep () {
        assert element != null: "NonEmptyList: Rep invariant, element non null";
        assert rest != null: "NonEmptyList: Rep invariant, rest non null";
        assert size == rest.size() + 1: "NonEmptyList: Rep invariant, size";        
    }

    private NonEmptyImList (E e, ImList<E> r) {
        element = e;
        rest = r;
        size = r.size() + 1;
    }

    public NonEmptyImList (E e) {
        assert e != null: "NonEmptyList(null)";
        element = e;
        rest = new EmptyImList<E>();
        size = 1;
        checkRep();
    }

    public ImList<E> add(E e) {
        assert e != null: "NonEmptyList.add(null)";
        return new NonEmptyImList<E> (e, this);
    }

    public E first() {
        return element;
    }

    public ImList<E> remove(E e) {
        assert e != null: "NonEmptyList.remove(null)";
        if (element.equals(e)) {
            return rest;
        }
        else {
            ImList<E> l = rest.remove (e);
            if (l == rest) return this;
            else return new NonEmptyImList<E> (element, l);
        }
    }

    public ImList<E> rest() {
        return rest;
    }

    public boolean contains (E e) {
        assert e != null: "NonEmptyList.contains(null)";
        return element.equals(e) || rest.contains(e);
    }

    public int size () {
        return size;
    }
    
    public boolean isEmpty () {
        return false;
    }

    public Iterator<E> iterator () {
        return new ImListIterator<E>(this);
    }
    

    /**
     * Compares the specified object with this list for equality.  Returns
     * <tt>true</tt> if the specified object is also a list, and the two lists
     * have the same elements in the same order.
     * 
     * @return all i | e_i.equals(eo_i) where this list = [e_0,...,e_n] and o = [eo_0,...,eo_n]
     */
    @Override
    public boolean equals (Object o) {
        if (o == this) return true;
        if (!(o instanceof ImList)) return false;
        ImList l = (ImList) o;
        if (l.size() != size()) return false;
        return first().equals(l.first()) && rest().equals(l.rest());
    }

    /**
     * Computes hash code
     * @return Returns the hash code value for this list. The hash code of a list is
     * defined to be the sum of the hash codes of the elements in the list,
     * where the hashcode of a <tt>null</tt> element is defined to be zero.
     */
    @Override
    public int hashCode () {
        return rest.hashCode() + (element == null ? 0 : element.hashCode());
    }

    /**
     * Get string representation of this list.
     * @return Returns the string representations of the list's elements, separated by commas,
     *    with the entire list surrounded by brackets.
     */
    @Override
    public String toString () {
        String s = "[";
        ImList<E> l = this;
        while (l.size() != 0) {
            if (l != this) s += ", ";
            s = s + l.first();
            l = l.rest();
        }
        return s + "]";
    }
}
