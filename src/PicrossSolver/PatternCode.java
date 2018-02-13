package PicrossSolver;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Describes a single line or column data entry (i. e. 2 2 2 or 1 2 4)
 * Must allow iterating over data values in appropriate order
 */
public class PatternCode implements Pattern{
    /**
     * Contains ordered data as integers. Left to right for lines, top to bottom for columns.
     */
    private LinkedList<Integer> data = new LinkedList<>();

    /**
     * Contains information whether the line/column described by this pattern is already solved. Initially: false.
     */
    private boolean solved;

    PatternCode(LinkedList<Integer> data, boolean solved){
        this.data = data;
        this.solved = solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public LinkedList<Integer> getData() {
        return data;
    }

    public boolean isSolved() {
        return solved;
    }

    public Iterator<Integer> iterator(){
        return this.getData().iterator();
    }

    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append("Border values: ");
        for(int i : data)
            ret.append(""+i+" ");
        ret.append("- Solved: "+solved+"");
        return ret.toString();
    }
}
