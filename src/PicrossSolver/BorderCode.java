package PicrossSolver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class BorderCode implements iBorder{
    List<iPattern> data = new LinkedList<>();

    public BorderCode(List<iPattern> data) {
        this.data = data;
    }

    public List<iPattern> getData() {
        return data;
    }

    public Iterator<iPattern> iterator(){
        return data.iterator();
    }


}
