package PicrossSolver;


import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.jsoup.Jsoup;

import java.io.File;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Contains entire picross pool, holding top and side borders as well as the binary matrix.
 */
public class Pool implements iPool {
    private iBorder top;
    private iBorder side;
    private iMatrix matrix;

    /**
     * Takes url to any hanje-star.com picross game and parses it into XML file
     * The file contains root "picross" with nodes "top" containing columns and "side" containing rows
     * @param url URL to picross game
     * @return XML file containing border codes
     */
    public static File toXML(String url) throws Exception{

            String document = Jsoup.connect(url).get().toString();
            String data =
                    document.substring(document.indexOf("{", document.indexOf("labels:"))+1,
                    document.indexOf("}", document.indexOf("labels:")));

            String top = data.split("\"")[3];
            String side = data.split("\"")[1];

            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("picross");
            doc.appendChild(rootElement);

            Element topCodes = doc.createElement("top");
            rootElement.appendChild(topCodes);

            Element sideCodes = doc.createElement("side");
            rootElement.appendChild(sideCodes);

            for(String col : top.split(";")){
                Element column = doc.createElement("column");
                column.appendChild(doc.createTextNode(col));
                topCodes.appendChild(column);
            }

            for(String row : side.split(";")){
                Element rowElement = doc.createElement("row");
                rowElement.appendChild(doc.createTextNode(row));
                sideCodes.appendChild(rowElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File("picross.xml");
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            return file;


    }

    public Pool(String url) throws Exception{
        this(toXML(url));
    }


    public Pool(File file) throws Exception{
        this(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file));
    }


    public Pool(Document doc) throws Exception{

            List<List<Integer>> topData = new LinkedList<>();
            List<List<Integer>> sideData = new LinkedList<>();

            Node top = doc.getElementsByTagName("top").item(0);
            Node side = doc.getElementsByTagName("side").item(0);

            for(int i = 0; i<top.getChildNodes().getLength(); i++){
                List<Integer> border = new LinkedList<>();
                String[] numbers = top.getChildNodes().item(i).getTextContent().split(",");
                for(String s : numbers){
                    border.add(Integer.parseInt(s));
                }
                topData.add(border);
            }

            for(int i = 0; i<side.getChildNodes().getLength(); i++){
                List<Integer> border = new LinkedList<>();
                String[] numbers = side.getChildNodes().item(i).getTextContent().split(",");
                for(String s : numbers){
                    border.add(Integer.parseInt(s));
                }
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
        //Pool.toXML("http://www.hanjie-star.com/picross/a-collection-of-keys-22243.html");
        try {
            Pool pool = new Pool("http://www.hanjie-star.com/picross/not-a-houndsooth-22157.html");
            pool.solve(false);
            System.out.println(pool);
            flushConsole();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
