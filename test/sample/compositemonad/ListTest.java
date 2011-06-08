package sample.compositemonad;

import static org.junit.Assert.assertEquals;
import static sample.compositemonad.List.l_flatten;
import static sample.compositemonad.List.l_map;
import static sample.compositemonad.List.l_unit;

import org.junit.Test;

import com.google.common.base.Function;

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
        System.out.println("ee = " + ee);
        List<Integer> result = l_flatten(ee);
        System.out.println("result = " + result);
        assertEquals(new List<Integer>(0, 1, -3, 7, 1, 2), result);
    }

    @Test
    public void testUnit() {
        assertEquals(new List<Integer>(1), l_unit(1));
        assertEquals(new List<String>("hoge"), l_unit("hoge"));
    }


    // 代数スタイルのモナド則
    // モナド則(1) l_flatten(l_unit(l)) = l
    @Test
    public void testMonad1() {
        List<Integer> list1 = new List<Integer>(1, -3, 7);
        assertEquals(l_flatten(l_unit(list1)), list1);
    }

    // モナド則(2) l_flatten(l_map(l_unit, l)) =l
    @Test
    public void testMonad2() {
        List<Integer> list1 = new List<Integer>(1, -3, 7);
        assertEquals(l_flatten(l_map(List.<Integer>l_unit(), list1)), list1);
    }

    // モナド則(3) l_flatten(l_flatten(lll)) = l_flatten(l_map(l_flatten, lll))
    @Test
    public void testMonad3() {
        List<List<List<Integer>>> lll = 
            new List<List<List<Integer>>>(
                new List<List<Integer>>(
                        new List<Integer>(0),
                        new List<Integer>(1, -3, 7),
                        new List<Integer>(1, 2)
                    ),
                new List<List<Integer>>(
                        new List<Integer>(3),
                        new List<Integer>(5, 6, 7),
                        new List<Integer>(2, 4)
                    )
            );
        System.out.println("lll = " + lll);
        assertEquals(
                l_flatten(l_flatten(lll)),
                l_flatten(l_map(List.<Integer>l_flatten(), lll)));
    }
}
