package PicrossSolver;

import java.util.LinkedList;
import java.util.List;

public interface iMatrix {
    public List<Cell> getColumn(int index);
    public List<Cell> getRow(int index);
    public boolean isSolved();
    public String toString();
}
