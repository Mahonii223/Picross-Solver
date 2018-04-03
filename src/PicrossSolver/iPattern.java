package PicrossSolver;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains methods of a single object describing border numbers of a picross pool, as well as an information whether or not given line/column is already solved.
 */
public interface iPattern extends Iterable{
    /**
     * Default single line toString method
     * @return String representaion of all data
     */
    String toString();

    List<Integer> getData();

}
