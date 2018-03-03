package PicrossSolver;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Pool pool = new Pool("http://www.hanjie-star.com/picross/another-knight-2--21868.html");
        List<Integer> example = new LinkedList<>();
        example.add(2);
        example.add(1);
        iPattern pat = new PatternCode(example,false);
        RowGenerator gen = new RowGenerator(pat, 7);
        for(List<Boolean> curr : gen){
            for(int i=0; i<curr.size(); i++){
                System.out.print(curr.get(i)+" ");
            }
            System.out.println();
        }
    }
}
