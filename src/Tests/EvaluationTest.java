package Tests;
import PicrossSolver.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;

public class EvaluationTest {
    @Test
    public void CorrectEvaluation(){


        //Tested pattern: 2,3,1,1,3,2 in length 17: should unequivocally result in combination: 11011101010111011
        List<Integer> data = new LinkedList<>();
        data.add(2);
        data.add(3);
        data.add(1);
        data.add(1);
        data.add(3);
        data.add(2);
        iPattern pat = new Pattern(data);

        List<Cell> row = new LinkedList<>();
        for(int i=0; i<17; i++){
            row.add(new Cell());
        }

        Pool.evaluate(pat, row);

        for(int i=0; i<17; i++) {
            assertEquals(true, row.get(i).isCertain());
        }
        assertTrue(row.get(0).getValue());
        assertTrue(row.get(1).getValue());
        assertFalse(row.get(2).getValue());
        assertTrue(row.get(3).getValue());
        assertTrue(row.get(4).getValue());
        assertTrue(row.get(5).getValue());
        assertFalse(row.get(6).getValue());
        assertTrue(row.get(7).getValue());
        assertFalse(row.get(8).getValue());
        assertTrue(row.get(9).getValue());
        assertFalse(row.get(10).getValue());
        assertTrue(row.get(11).getValue());
        assertTrue(row.get(12).getValue());
        assertTrue(row.get(13).getValue());
        assertFalse(row.get(14).getValue());
        assertTrue(row.get(15).getValue());
        assertTrue(row.get(16).getValue());
    }

    @Test
    void ExceptionTesting(){
        //Testing pattern: 1 against row 1xxx1
        //Should throw exception
        List<Integer> data = new LinkedList<>();
        data.add(1);
        iPattern pat = new Pattern(data);
        List<Cell> row = new LinkedList<>();
        for(int i=0; i<5; i++)
            row.add(new Cell());

        row.get(0).setCertain(true);
        row.get(4).setCertain(true);
        row.get(0).setValue(true);
        row.get(4).setValue(true);

        boolean exceptionThrown = false;

        try{
            Pool.evaluate(pat, row);
        }
        catch(InputMismatchException e){
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }
}
