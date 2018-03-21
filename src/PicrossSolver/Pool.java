package PicrossSolver;


import org.jsoup.Jsoup;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains entire picross pool, holding top and side borders as well as the binary matrix.
 */
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
                iPattern pattern = new PatternCode(pat);
                topPatterns.add(pattern);
            }

            for(List<Integer> pat : sideData){
                iPattern pattern = new PatternCode(pat);
                sidePatterns.add(pattern);
            }

            this.top = new BorderCode(topPatterns);
            this.side = new BorderCode(sidePatterns);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Creates and ASCII representation of the entire pool
     * @return string containing the representation
     */
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

        //Appending top border
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

        //appending side border and matrix
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

    public static void flushConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void solve(boolean display){
        matrix = new Matrix(side.getData().size(), top.getData().size());
        int counter = 0;
        while(!matrix.isSolved()&&scan(display)){
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

    public boolean scan(boolean display){
        try {
            int index = 0;
            boolean changed = false;
            for (iPattern pattern : top.getData()) {
                if (evaluate(pattern, matrix.getColumn(index)))
                    changed = true;
                index++;
                System.out.println("Checking column: " + index);
                System.out.println(this.toString());


                if (display) {
                    flushConsole();

                    System.out.println(matrix.toString());
                }
            }
            index = 0;

            for (iPattern pattern : side.getData()) {
                if (evaluate(pattern, matrix.getRow(index)))
                    changed = true;
                index++;
                System.out.println("Checking row: " + index);
                System.out.println(this.toString());

                if (display) {
                    flushConsole();

                    System.out.println(matrix.toString());
                }
            }

            return changed;
        }
        catch(InputMismatchException e){
            System.out.println("Incorrect pattern - picross cannot be solved");
            return false;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Evaluates and updates a single row by iterating through every possible combination and finding common blocks among the matching ones
     * @param pattern border data
     * @param row List of cells
     * @return true, if any uncertain cell has been made certain. Otherwise false
     */
    public static boolean evaluate(iPattern pattern, List<Cell> row) throws InputMismatchException{

        //Create a list corresponding with with provided row.
        List<Boolean> certain = new LinkedList<>();
        for(int i=0; i<row.size(); i++){
            //At first the list contains only true values, because the first matching row combination has no predecessors, and thus nothing to compare it to.
            certain.add(true);
        }

        //The reference to the last iterated pattern must be saved in order to be compared with the next one.
        List<Boolean> last = null;

        //Creating a new RowGenerator - the generator will allow to easily iterate through all possible row combinations as Boolean Lists.
        RowGenerator generator = new RowGenerator(pattern, row.size());

        for(List<Boolean> current : generator){

            //Current row combination is compared with row cells that are already certain.
            if(!conflict(row, current)){
                //If current combination matches certain cells, it is compared with the last matching combination
                if(last != null)
                {
                    for(int i = 0; i<current.size(); i++){
                        //If a given cell with the same index has different values in two different matching combinations,
                        //it is safe to assume that the value of this cell cannot be determined in this step
                        //Thus, the cell is marked as uncertain.
                        if(current.get(i)!=last.get(i))
                            certain.set(i, false);
                    }
                }
                //Reference to the last combination is updated
                last = current;
            }
        }

        //At this point, list certain corresponds to list last and marks every cell that had the same value throughout
        //every matching combination. These cells are certain and can be updated.

        boolean rowWasUpdated = false;

        //If last holds null, it is clear that none of the possible combinations match certain row cells
        //This indicates a mismatch between border pattern and row
        if(last == null)
            throw new InputMismatchException();

        for(int i=0; i<certain.size(); i++){
            if(certain.get(i)) {
                if (!row.get(i).isCertain()) {
                    rowWasUpdated = true;
                    row.get(i).setCertain(true);
                    row.get(i).setValue(last.get(i));
                }

            }

        }
        System.out.println();

        return rowWasUpdated;
    }

    private static boolean conflict(List<Cell> row, List<Boolean> checked){

        for(int i = 0; i<row.size(); i++) {

            if (row.get(i).isCertain() && row.get(i).getValue() != checked.get(i)){
                return true;
            }

        }

        return false;

    }


    public static void main(String[] args) {
        Pool pool = new Pool("http://www.hanjie-star.com/picross/not-a-houndsooth-22157.html");
        pool.solve(false);
        flushConsole();
    }
}
