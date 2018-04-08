package PicrossSolver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Describes a single line or column data entry (i. e. 2 2 2 or 1 2 4)
 * Must allow iterating over data values in appropriate order
 */
public class Pattern implements iPattern{
    /**
     * Contains ordered data as integers. Left to right for lines, top to bottom for columns.
     */
    private List<Integer> data = new LinkedList<>();

    public Pattern(List<Integer> data){
        this.data = data;
    }

    /**
     * Getters and setters
     */

    public List<Integer> getData() {
        return data;
    }


    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append("Border values: ");
        for(int i : data)
            ret.append(i+" ");
        return ret.toString();
    }

    @Override
    public Iterator<Integer> iterator(){
        return data.iterator();
    }

    public int size(){
        return data.size();
    }

    public Integer get(int index){
        return data.get(index);
    }


}

