package sample.compositemonad;

import static org.junit.Assert.assertEquals;
import static sample.compositemonad.List.l_flatten;
import static sample.compositemonad.List.l_map;
import static sample.compositemonad.List.l_unit;

import org.junit.Test;

import com.google.common.base.Function;

@SuppressWarnings("unchecked")
public class ListTest {

	@Test
	public void testMap() {
        List<Integer> list1 = new List<Integer>(1, -3, 7);
        System.out.println("list1 = " + list1);
        Function<Integer, Integer> add1 = new Function<Integer, Integer>() {
            public Integer apply(Integer x) {
                return Integer.valueOf(x.intValue() + 1);
            }
        };
        List<Integer> list2 = l_map(add1, list1);
        System.out.println("list2 = " + list2);
        assertEquals(new List<Integer>(2, -2, 8), list2);
	}

	@Test
	public void testFlatten() {
		List<List<Integer>> ee =
            new List<List<Integer>>(
                new List<Integer>(0),
                new List<Integer>(1, -3, 7),
                new List<Integer>(1, 2)
            );
		List<Integer> result = l_flatten(ee);
        System.out.println("result = " + result);
        assertEquals(new List<Integer>(0, 1, -3, 7, 1, 2), result);
	}

	@Test
	public void testUnit() {
        assertEquals(new List<Integer>(1), l_unit(1));
        assertEquals(new List<String>("hoge"), l_unit("hoge"));
	}

}
