package PicrossSolver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides easy iteration over all possible combinations in a row for a given pattern
 * I.e.: For pattern 1,2 and length 5: 10011, 10110, 01011
 */
public class RowGenerator implements Iterable<List<Boolean>>{

    //Holds code pattern
    private final iPattern pattern;

    //Holds row length
    private final int length;

    //Will contain calculated sum of white spots decreased by 1 for each gap between black spots
    //To put this on other way: The basic row constructed by standalone pattern contains black sections with lengths specified by the pattern
    //Separated by obligatory single white spots
    //The rest of white spots, which need to be distributed between the gaps are summed up in this variable
    private final int gapLength;

    //Number of gaps, which is naturally number of black sections increased by 1
    private final int gapNum;

    public RowGenerator(iPattern pattern, int length){

        this.pattern = pattern;
        this.length = length;

        //Sum of all black spots in this row to be calculated
        int blackSum = 0;

        for(Integer i : pattern){

            blackSum+=i;

        }

        //number of black sections in this row
        int counter = pattern.size();

        gapNum = counter +1;

        if(counter != 0)
            counter--;

        gapLength = length - blackSum - counter;

    }


    @Override
    public Iterator<List<Boolean>> iterator(){
        List<Integer> gapConfig = new LinkedList<>();

        for(int i=0; i<gapNum; i++){
            gapConfig.add(0);
        }

        if(gapConfig.size()>0)
            gapConfig.set(0, -1);


        Iterator<List<Boolean>> it = new Iterator<List<Boolean>>() {

            @Override
            public boolean hasNext(){

                if(gapConfig.size()==0) {
                    return false;
                }

                if(gapConfig.get(0)==-1) {
                    gapConfig.set(0, gapLength);
                    return true;
                }

                if(gapConfig
                        .get(gapConfig.size()-1) == gapLength) {
                    return false;
                }


                //at this point it is safe to assume the number of gaps is two or higher
                //It is also safe to assume that the first non-zero gap we encounter will not be the last



                    int i = 0;
                    while (gapConfig.get(i) == 0)
                        i++;
                    gapConfig.set(i + 1, gapConfig.get(i + 1) + 1);
                    int current = gapConfig.get(i) - 1;
                    gapConfig.set(i, 0);
                    gapConfig.set(0, current);


                return true;
            }

            @Override
            public List<Boolean> next(){

                List<Boolean> conf = new LinkedList<>();

                for(int confIndex=0; confIndex < gapConfig.size()-1; confIndex++){

                    for(int a = 0; a < gapConfig.get(confIndex); a++)
                        conf.add(false);

                    if(confIndex>0)
                        conf.add(false);

                    for(int a = 0; a < pattern.get(confIndex); a++)
                        conf.add(true);
                }

                for(int a = 0; a < gapConfig.get(gapConfig.size() - 1); a++)
                    conf.add(false);

                return conf;
            }

        };

        return it;
    }


}
