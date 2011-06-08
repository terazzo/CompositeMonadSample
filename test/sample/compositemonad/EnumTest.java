package sample.compositemonad;

import static org.junit.Assert.assertEquals;
import static sample.compositemonad.Enum.e_flatten;
import static sample.compositemonad.Enum.e_map;
import static sample.compositemonad.Enum.e_unit;

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
        System.out.println("ee = " + ee);
        Enum<Integer> result = e_flatten(ee);
        System.out.println("result = " + result);
        assertEquals(new Enum<Integer>(0, 1, -3, 7, 2), result);
    }
    @Test
    public void testUnit() {
        assertEquals(new Enum<Integer>(1), e_unit(1));
        assertEquals(new Enum<String>("hoge"), e_unit("hoge"));
    }

    // 代数スタイルのモナド則
    // モナド則(1) e_flatten(e_unit(e)) = e
    @Test
    public void testMonad1() {
        Enum<Integer> enum1 = new Enum<Integer>(1, -3, 7);
        assertEquals(e_flatten(e_unit(enum1)), enum1);
    }

    // モナド則(2) e_flatten(e_map(e_unit, e)) = e
    @Test
    public void testMonad2() {
        Enum<Integer> enum1 = new Enum<Integer>(1, -3, 7);
        assertEquals(e_flatten(e_map(Enum.<Integer>e_unit(), enum1)), enum1);
    }

    // モナド則(3) e_flatten(e_flatten(eee)) = e_flatten(e_map(e_flatten, eee))
    @Test
    public void testMonad3() {
        Enum<Enum<Enum<Integer>>> eee = 
            new Enum<Enum<Enum<Integer>>>(
                new Enum<Enum<Integer>>(
                        new Enum<Integer>(0),
                        new Enum<Integer>(1, -3, 7),
                        new Enum<Integer>(1, 2)
                    ),
                new Enum<Enum<Integer>>(
                        new Enum<Integer>(3),
                        new Enum<Integer>(5, 6, 7),
                        new Enum<Integer>(2, 4)
                    )
            );
        System.out.println("eee = " + eee);
        assertEquals(
                e_flatten(e_flatten(eee)),
                e_flatten(e_map(EnumOfList.<Integer>e_flatten(), eee)));
    }
}
