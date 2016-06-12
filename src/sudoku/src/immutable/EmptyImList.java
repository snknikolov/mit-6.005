/**
 * An immutable list
 * Designed for illustrating reasoning about immutable types
 * 
 * Copyright 2007 Daniel Jackson and MIT
 */
package sudoku.src.immutable;

import java.util.Iterator;

public class EmptyImList<E> implements ImList<E> {
    /**
     * abstraction function A(this) = <>, the empty list
     * 
     * rep invariant = true
     */

    public EmptyImList() {
    }

    public ImList<E> add(E e) {
        assert e != null : "EmptyList.add(null)";
        return new NonEmptyImList<E>(e);
    }

    public ImList<E> remove(E e) {
        assert e != null : "EmptyList.remove(null)";
        return this;
    }

    public E first() {
        assert false : "EmptyList.first";
        return null;
    }

    public ImList<E> rest() {
        assert false : "EmptyList.rest";
        return null;
    }

    public boolean contains(E e) {
        assert e != null : "EmptyList.contains(null)";
        return false;
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    public Iterator<E> iterator() {
        return new ImListIterator<E>(this);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EmptyImList;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "[]";
    }
}
