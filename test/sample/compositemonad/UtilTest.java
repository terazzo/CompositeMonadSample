package sample.compositemonad;
import static org.junit.Assert.assertEquals;
import static sample.compositemonad.Util.combinations;

import org.junit.Test;


@SuppressWarnings("unchecked")
public class UtilTest {
    @Test
    public void testCombinations1() {
        List<Enum<Integer>> le = new List<Enum<Integer>>(
            new Enum<Integer>(0),
            new Enum<Integer>(1, -3, 7),
            new Enum<Integer>(1, 2)
        );
        System.out.println("le = " + le);
        Enum<List<Integer>> el = combinations(le);
        System.out.println("el = " + el);
        
        Enum<List<Integer>> expected = new Enum<List<Integer>>(
                new List<Integer>(0, 1, 2),
                new List<Integer>(0, 1, 1),
                new List<Integer>(0, -3, 1),
                new List<Integer>(0, -3, 2),
                new List<Integer>(0, 7, 1),
                new List<Integer>(0, 7, 2)
        );
        assertEquals(expected, el);
    }
    @Test
    public void testCombinations2() {
        List<Enum<Integer>> le = new List<Enum<Integer>>(
            new Enum<Integer>(0),
            new Enum<Integer>(),
            new Enum<Integer>(1, 2)
        );
        System.out.println("le = " + le);
        Enum<List<Integer>> el = combinations(le);
        System.out.println("el = " + el);
        assertEquals(new Enum<List<Integer>>() , el);
    }
 
}
