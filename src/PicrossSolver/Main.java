package PicrossSolver;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Pool pool = new Pool("http://www.hanjie-star.com/picross/another-knight-2--21868.html");
        /*
        List<Integer> ex= new LinkedList<>();
        ex.add(2);
        ex.add(3);
        ex.add(3);
        ex.add(2);
        ex.add(1);

        iPattern pattern = new PatternCode(ex, false);
        List<Cell> row = new LinkedList<>();
        for(int i=0; i<16; i++)
            row.add(new Cell());

        while(Pool.evaluate(pattern, row))
        {
            System.out.println("processing");
        }
        */
        //System.out.println(pool.toString());
        pool.solve(false);
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
