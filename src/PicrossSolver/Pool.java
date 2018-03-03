package PicrossSolver;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class Pool implements iPool {
    iBorder top;
    iBorder side;
    iMatrix matrix;

    public Pool(String url){
        try {
            String document = Jsoup.connect(url).get().toString();
            String data = document.substring(document.indexOf("{", document.indexOf("labels:"))+1,
                    document.indexOf("}", document.indexOf("labels:")));

            String top = data.split("\"")[3];
            String side = data.split("\"")[1];

            List<List<Integer>> topData = new LinkedList<>();
            List<List<Integer>> sideData = new LinkedList<>();

            for(String i : top.split(";")){
                List<Integer> border = new LinkedList<>();
                for(String a : i.split(","))
                    border.add(Integer.parseInt(a));
                topData.add(border);
            }

            for(String i : side.split(";")){
                List<Integer> border = new LinkedList<>();
                for(String a : i.split(","))
                    border.add(Integer.parseInt(a));
                sideData.add(border);
            }

            List<iPattern> topPatterns = new LinkedList<>();
            List<iPattern> sidePatterns = new LinkedList<>();

            for(List<Integer> pat : topData){
                iPattern pattern = new PatternCode(pat, false);
                topPatterns.add(pattern);
            }

            for(List<Integer> pat : sideData){
                iPattern pattern = new PatternCode(pat, false);
                sidePatterns.add(pattern);
            }

            this.top = new BorderCode(topPatterns);
            this.side = new BorderCode(sidePatterns);

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public String toString(){
        StringBuilder builder = new StringBuilder();
        int maxLenTop = 0;
        int maxLenSide = 0;

        //determining longest top pattern
        for(iPattern pattern : this.top.getData()){
            if(pattern.getData().size()>maxLenTop)
                maxLenTop = pattern.getData().size();
        }

        //determining longest side pattern
        for(iPattern pattern : this.side.getData()){
            if(pattern.getData().size()>maxLenSide)
                maxLenSide = pattern.getData().size();
        }

        //invoking matrix content
        String[] matrixContent = null;
        if(matrix!=null)
            matrixContent = matrix.toString().split("\n");


        //Appending top marigin
        for(int i = 0; i<maxLenTop; i++){
            for(int a = 0; a<maxLenSide*3; a++){
                builder.append(" ");
            }
            builder.append('|');
            for(iPattern pattern : top.getData()){
                if(pattern.getData().size()-maxLenTop+i>=0)
                    builder.append(String.format("%02d", pattern.getData().get(pattern.getData().size()-maxLenTop+i)));
                else
                    builder.append("  ");
                builder.append('|');
            }
            builder.append('\n');
        }

        //appending side marigin and matrix
        int lineIndex = 0;

        for(iPattern pattern : side.getData()){
            for(int i = 0; i<maxLenSide; i++) {
                if (pattern.getData().size() - maxLenSide + i >= 0)
                    builder.append(String.format("%02d", pattern.getData().get(pattern.getData().size() - maxLenSide + i)));
                else
                    builder.append("  ");
                builder.append('|');
            }
            if(matrixContent!=null) {
                builder.append(matrixContent[lineIndex]);
            }
            builder.append('\n');
            lineIndex++;
        }
        return builder.toString();
    }


    public void solve(boolean display){
        matrix = new Matrix(side.getData().size(), top.getData().size());
        int counter = 0;
        while(!matrix.isSolved()&&iterate(display)){
            counter++;
            System.out.println("Evaluation index: "+counter);
        }
        if(display) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Iterations: " + counter);
            if (!matrix.isSolved()) {
                System.out.println("Pool could not be solved.");
            }
            System.out.println(toString());
        }
    }

    public boolean iterate(boolean display){
        int index = 0;
        boolean changed = false;
        for(iPattern pattern : top.getData()){
            if(evaluate(pattern, matrix.getColumn(index)))
                changed = true;
            index++;
            System.out.println("Checking column: "+index);
            System.out.println(this.toString());


            if(display) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                System.out.println(matrix.toString());
            }
        }
        index = 0;

        for(iPattern pattern : side.getData()){
            if(evaluate(pattern, matrix.getRow(index)))
                changed = true;
            index++;
            System.out.println("Checking row: "+index);
            System.out.println(this.toString());

            if(display) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                System.out.println(matrix.toString());
            }
        }

        return changed;
    }

    public static boolean evaluate(iPattern pattern, List<Cell> row){
        /*
        System.out.println("New evaluation");
        System.out.println(pattern);
        for(Cell c : row){
            if(!c.isCertain())
                System.out.print("unkno ");
            else
                if(c.getValue())
                    System.out.print("True  ");
                else
                    System.out.print("False ");
        }
        System.out.println();
        */
        List<Boolean> certain = new LinkedList<>();
        for(int i=0; i<row.size(); i++){
            certain.add(true);
        }

        List<Boolean> last = null;

        RowGenerator generator = new RowGenerator(pattern, row.size());

        for(List<Boolean> current : generator){
            if(!conflict(row, current)){
                //System.out.println("No conflict");
                if(last == null)
                    last = current;
                else{
                    for(int i = 0; i<current.size(); i++){
                        if(current.get(i)!=last.get(i))
                            certain.set(i, false);
                    }

                }
            }
            //else
              //  System.out.println("Conflict");
        }

        boolean flag = false;



        if(last == null)
            System.out.println("last is null");

        //System.out.println(row.size()+"  "+last.size());

        for(int i=0; i<certain.size(); i++){
            if(certain.get(i)) {
                if (!row.get(i).isCertain()) {
                    flag = true;
                    row.get(i).setCertain(true);
                    row.get(i).setValue(last.get(i));
                }

                /*if(last.get(i))
                    System.out.print("true  ");
                else
                    System.out.print("false ");
            }
            else
                System.out.print("Unkno ");
                */
            }

        }
        System.out.println();

        return flag;
    }

    static boolean conflict(List<Cell> row, List<Boolean> checked){


        for(int i = 0; i<row.size(); i++) {
            if (row.get(i).isCertain() && row.get(i).getValue() != checked.get(i)){
                return true;
            }

        }
        return false;
    }

    public static void main(String[] args) {
        Pool pool = new Pool("http://www.hanjie-star.com/picross/winter-scene-21805.html");
        pool.solve(false);
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}