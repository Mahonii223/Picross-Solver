package Tests;
import PicrossSolver.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

public class EvaluationTest {
    @Test
    public void CorrectEvaluation(){
        List<Integer> data = new LinkedList<>();

        //Tested pattern: 2,3,1,1,3,2 in length 17: should unequivocally result in combination: 11011101010111011
        data.add(2);
        data.add(3);
        data.add(1);
        data.add(1);
        data.add(3);
        data.add(2);
        iPattern pat = new PatternCode(data);

        List<Cell> row = new LinkedList<>();
        for(int i=0; i<17; i++){
            row.add(new Cell());
        }

        Pool.evaluate(pat, row);

        for(int i=0; i<17; i++) {
            assertEquals(true, row.get(i).isCertain());
        }
        assertEquals(true, row.get(0).getValue());
        assertEquals(true, row.get(1).getValue());
        assertEquals(false, row.get(2).getValue());
        assertEquals(true, row.get(3).getValue());
        assertEquals(true, row.get(4).getValue());
        assertEquals(true, row.get(5).getValue());
        assertEquals(false, row.get(6).getValue());
        assertEquals(true, row.get(7).getValue());
        assertEquals(false, row.get(8).getValue());
        assertEquals(true, row.get(9).getValue());
        assertEquals(false, row.get(10).getValue());
        assertEquals(true, row.get(11).getValue());
        assertEquals(true, row.get(12).getValue());
        assertEquals(true, row.get(13).getValue());
        assertEquals(false, row.get(14).getValue());
        assertEquals(true, row.get(15).getValue());
        assertEquals(true, row.get(16).getValue());
    }
}
