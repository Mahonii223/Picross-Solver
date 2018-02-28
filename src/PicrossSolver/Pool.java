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


        for(iPattern pattern : this.top.getData()){
            if(pattern.getData().size()>maxLenTop)
                maxLenTop = pattern.getData().size();
        }
        for(iPattern pattern : this.side.getData()){
            if(pattern.getData().size()>maxLenSide)
                maxLenSide = pattern.getData().size();
        }

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

        int currentIndex = 0;
        for(iPattern pattern : side.getData()){
            for(int i = 0; i<maxLenSide; i++) {
                if (pattern.getData().size() - maxLenSide + i >= 0)
                    builder.append(String.format("%02d", pattern.getData().get(pattern.getData().size() - maxLenSide + i)));
                else
                    builder.append("  ");
                builder.append('|');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
