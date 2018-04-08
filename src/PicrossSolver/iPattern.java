package PicrossSolver;

import java.util.Iterator;
import java.util.List;

/**
 * Contains methods of a single object describing border numbers of a picross pool, as well as an information whether or not given line/column is already solved.
 */
public interface iPattern extends Iterable<Integer>{
    /**
     * Default single line toString method
     * @return String representaion of all data
     */
    String toString();

    int size();

    Integer get(int index);

    @Override
    Iterator<Integer> iterator();
}
