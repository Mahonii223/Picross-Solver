package Tests;
import PicrossSolver.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ConstructorTest {

    @Test
    public void URLConstructorTest(){
        String url = "nonsense";
        try {
            Pool pool = new Pool(url);
            assertNull(pool);
        }
        catch(Exception e){
        }
        url = "http://www.hanjie-star.com/picross/another-knight-2--21868.html";
        try {
            Pool pool = new Pool(url);
            assertNotNull(pool);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
