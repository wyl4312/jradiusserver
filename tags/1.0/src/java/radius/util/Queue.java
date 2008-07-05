package radius.util;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class Queue {
    private LinkedList content = new LinkedList();

    public void unshift(Object o) {
        content.addFirst(o);
    }

    public Object shift() {
        if (!content.isEmpty())
            return content.removeFirst();
        else
            return null;
    }

    public void push(Object o) {
        content.addLast(o);
    }

    public Object pop() {
        if (!content.isEmpty())
            return content.removeLast();
        else
            return null;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public int size() {
        return content.size();
    }

    public Iterator iterator() {
        return content.iterator();
    }
}