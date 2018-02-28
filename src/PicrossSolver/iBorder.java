package PicrossSolver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Interface of a class whose object describes a top or side complete set of data.
 */
public interface iBorder extends Iterable {
    public List<iPattern> getData();
    public Iterator<iPattern> iterator();
}
