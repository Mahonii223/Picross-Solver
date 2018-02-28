package PicrossSolver;

import java.util.LinkedList;

public class Matrix {

    //Contains a linear representation of the whole bit array. Two-dimensional array is being representedo onto a one-dimensional LinekedList in reading order (left to right, top to bottom into ledt to right)
    private LinkedList<Cell> linear = new LinkedList<>();
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
}
