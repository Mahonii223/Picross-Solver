package PicrossSolver;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public interface iPool {

    String toString();

    /**
     * Takes url to any hanje-star.com picross game and parses it into XML file
     * The file contains root "picross" with nodes "top" containing columns and "side" containing rows
     * @param url URL to picross game
     * @return XML file containing border codes
     */
    static File toXML(String url) throws Exception{

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

    void solve();

    boolean scan();
}
