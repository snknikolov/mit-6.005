/**
 * Date: Mar 6, 2008, 1:31:54 PM
 * 6.005 Elements of Software Construction
 * (c) 2008, MIT and Daniel Jackson
 */
package sudoku.src.immutable;
import java.util.Iterator;

/**
 * Implementation of an iterator for immutable lists
 */
public class ImListIterator<E> implements Iterator<E> {
    /* rep invariant
     * remaining != null
     */
    ImList<E> remaining;

    public ImListIterator (ImList<E> list) {
        remaining = list;
    }

    public boolean hasNext () {
        return !remaining.isEmpty();
    }

    public E next () {
        E first = remaining.first ();
        remaining = remaining.rest();
        return first;
    }
    
    public void remove () {
        throw new UnsupportedOperationException ();
    }
}
