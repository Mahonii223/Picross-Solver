package PicrossSolver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class RowGenerator implements Iterable<List<Boolean>>{
    private iPattern pattern;
    private int length;
    private int gapLength;
    private int gapNum;

    public RowGenerator(iPattern pattern, int length){

        this.pattern = pattern;
        this.length = length;

        int blackSum = 0;
        int counter = 0;

        for(Integer i : pattern.getData()){

            blackSum+=i;
            counter++;

        }

        gapNum = counter +1;

        if(counter != 0)
            counter--;

        gapLength = length - blackSum - counter;

    }

    @Override
    public Iterator<List<Boolean>> iterator(){
        List<Integer> gapConf = new LinkedList<>();

        for(int i=0; i<gapNum; i++){
            gapConf.add(0);
        }

        if(gapConf.size()>0)
            gapConf.set(0, -1);


        Iterator<List<Boolean>> it = new Iterator<List<Boolean>>() {

            @Override
            public boolean hasNext(){

                if(gapConf.size()==0) {
                    return false;
                }

                if(gapConf.get(0)==-1) {
                    gapConf.set(0, gapLength);
                    return true;
                }

                if(gapConf.get(gapConf.size()-1)==gapLength) {
                    return false;
                }


                //at this point it is safe to assume the number of gaps is two or higher
                //It is also safe to assume that the first non-zero gap we encounter will not be the last



                    int i = 0;
                    while (gapConf.get(i) == 0)
                        i++;
                    gapConf.set(i + 1, gapConf.get(i + 1) + 1);
                    int current = gapConf.get(i) - 1;
                    gapConf.set(i, 0);
                    gapConf.set(0, current);


                return true;
            }

            @Override
            public List<Boolean> next(){

                List<Boolean> conf = new LinkedList<>();

                for(int confIndex=0; confIndex < gapConf.size()-1; confIndex++){

                    for(int a = 0; a < gapConf.get(confIndex); a++)
                        conf.add(false);

                    if(confIndex>0)
                        conf.add(false);

                    for(int a = 0; a < pattern.getData().get(confIndex); a++)
                        conf.add(true);
                }

                for(int a = 0; a < gapConf.get(gapConf.size() - 1); a++)
                    conf.add(false);

                return conf;
            }

        };

        return it;
    }


}
