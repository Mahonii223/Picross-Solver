package PicrossSolver;

import java.util.LinkedList;
import java.util.List;

public class Matrix implements iMatrix{

    //Contains a linear representation of the whole bit array. Two-dimensional array is being representedo onto a one-dimensional LinekedList in reading order (left to right, top to bottom into ledt to right)
    private List<Cell> linear = new LinkedList<>();
    private int length;
    private int width;

    public Matrix(int length, int width){
        this.length = length;
        this.width = width;
        for(int i=0; i<width*length; i++){
            linear.add(new Cell());
        }
    }

    public Cell get(int x, int y){
        if(x<0||x>=width||y<0||y>=length)
            return null;
        return linear.get(y*width+x);
    }

    public List<Cell> getColumn(int index){
        List<Cell> column = new LinkedList<>();
        for(int i=0; i<length; i++){
            column.add(linear.get(index));
            index+=width;
        }
        return column;
    }

    public List<Cell> getRow(int index){
        List<Cell> row = new LinkedList<>();
        for(int i=0; i<width; i++){
            row.add(get(i, index));
        }
        return row;
    }

    public boolean isSolved(){
        for(Cell c : linear){
            if(!c.isCertain())
                return false;
        }
        return true;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<width*length; i++){
            String curr = " __";
            if(linear.get(i).isCertain()){
                if(linear.get(i).getValue())
                    curr = " XX";
                else
                    curr = "   ";
            }
            builder.append(curr);
            if((i+1)%width == 0)
                builder.append('\n');
        }

        return builder.toString();
    }
}
