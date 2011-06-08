package sample.compositemonad;

import static org.junit.Assert.assertEquals;
import static sample.compositemonad.Enum.e_flatten;
import static sample.compositemonad.Enum.e_map;

import org.junit.Test;

import com.google.common.base.Function;


@SuppressWarnings("unchecked")
public class EnumTest {
    @Test
    public void testMap() {
        Enum<Integer> enum1 = new Enum<Integer>(1, -3, 7);
        System.out.println("enum1 = " + enum1);
        Function<Integer, Integer> add1 = new Function<Integer, Integer>() {
            public Integer apply(Integer x) {
                return Integer.valueOf(x.intValue() + 1);
            }
        };
        Enum<Integer> enum2 = e_map(add1, enum1);
        System.out.println("enum2 = " + enum2);
        assertEquals(new Enum<Integer>(2, -2, 8), enum2);
    }
    @Test
    public void testFlatten() {
        Enum<Enum<Integer>> ee =
            new Enum<Enum<Integer>>(
                new Enum<Integer>(0),
                new Enum<Integer>(1, -3, 7),
                new Enum<Integer>(1, 2)
            );
        Enum<Integer> result = e_flatten(ee);
        System.out.println("result = " + result);
        assertEquals(new Enum<Integer>(0, 1, -3, 7, 2), result);
    }


}
