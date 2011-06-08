package sample.compositemonad;
import static org.junit.Assert.assertEquals;
import static sample.compositemonad.Util.combinations;
import static sample.compositemonad.List.*;
import static sample.compositemonad.Enum.*;

import org.junit.Test;


@SuppressWarnings("unchecked")
public class UtilTest {
    @Test
    public void testCombinations() {
        List<Enum<Integer>> le = new List<Enum<Integer>>(
            new Enum<Integer>(0),
            new Enum<Integer>(1, -3, 7),
            new Enum<Integer>(1, 2)
        );
        System.out.println("le = " + le);
        Enum<List<Integer>> el = combinations(le);
        System.out.println("el = " + el);
        
        Enum<List<Integer>> expected = new Enum<List<Integer>>(
                new List<Integer>(0, 1, 1),
                new List<Integer>(0, 1, 2),
                new List<Integer>(0, -3, 1),
                new List<Integer>(0, -3, 2),
                new List<Integer>(0, 7, 1),
                new List<Integer>(0, 7, 2)
        );
        assertEquals(expected, el);
    }
    @Test
    public void testCombinations_withEmpty() {
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
    // combinations(l_unit(enu)) = e_map(l_unit, enu)
    @Test
    public void testBeckTheorem1() {
    	Enum<Integer> enu = new Enum<Integer>(1, -3, 7);
    	assertEquals(
			combinations(List.<Enum<Integer>>l_unit(enu)),
			e_map(List.<Integer>l_unit(), enu)
		);
    }
    // combinations(l_map(e_unit, lis)) = e_unit(lis)
    @Test
    public void testBeckTheorem2() {
    	List<Integer> lis = new List<Integer>(1, -3, 7);
    	assertEquals(
			combinations(l_map(Enum.<Integer>e_unit(), lis)),
			e_unit(lis)
		);
    }
    // e_flatten(e_map(combinations, combinations(lee))) = combinations(l_map(e_flatten, lee))
    @Test
    public void testBeckTheorem3() {
    	List<Enum<Enum<Integer>>> lee = new List<Enum<Enum<Integer>>>(
                new Enum<Enum<Integer>>(
                    new Enum<Integer>(0),
                    new Enum<Integer>(1, -3, 7),
                    new Enum<Integer>(4, 5)
            	),
                new Enum<Enum<Integer>>(
		            new Enum<Integer>(0),
		            new Enum<Integer>(1, 2)
            	)
            );
    	assertEquals(
			e_flatten(e_map(Util.<Integer, Enum<Integer>>combinations(), combinations(lee))),
			combinations(l_map(Enum.<Integer>e_flatten(), lee))
		);
    }
    // e_map(l_flatten, combinations(l_map(combinations, lle))) = combinations(l_flatten(lle))
    @Test
    public void testBeckTheorem4() {
    	List<List<Enum<Integer>>> lle = new List<List<Enum<Integer>>>(
                new List<Enum<Integer>>(
                    new Enum<Integer>(0),
                    new Enum<Integer>(1, -3, 7),
                    new Enum<Integer>(4, 5)
            	),
                new List<Enum<Integer>>(
		            new Enum<Integer>(0),
		            new Enum<Integer>(1, 2)
            	)
            );
    	assertEquals(
    			e_map(List.<Integer>l_flatten(), combinations(l_map(Util.<Integer, Enum<Integer>>combinations(), lle))),
    			combinations(l_flatten(lle))
		);
    }

}
