package sample.compositemonad;

import static org.junit.Assert.assertEquals;
import static sample.compositemonad.Enum.e_bind;
import static sample.compositemonad.Enum.e_flatten;
import static sample.compositemonad.Enum.e_map;
import static sample.compositemonad.Enum.e_unit;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Function;


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

    Function<Long, Enum<Long>> sum1 = new Function<Long, Enum<Long>>() {
        public Enum<Long> apply(Long x) {
            return e_unit(Long.valueOf(x.longValue() + 1));
        }
    };
    Function<Long, Enum<Long>> factors = new Function<Long, Enum<Long>>() {
        public Enum<Long> apply(Long n) {
            if (n < 1) {
                throw new IllegalArgumentException("arg should be positive.");
            }
            if (n == 1) {
                return new Enum<Long>(1L);
            }
            Set<Long> result = new HashSet<Long>();
            long k = n;
            for (long i = 2, r = Math.round(Math.sqrt(k)); i <= r; i ++) {
                while (k % i == 0) {
                    result.add(i);
                    k /= i;
                    r = (int) Math.round(Math.sqrt(k));
                }
            }
            if (k != 1) {
                result.add(k);
            }
            return new Enum<Long>(result);
        }
    };
    @Test
    public void testFactors() {
        for (long i = 2; i < 100; i++) {
            System.out.printf("factors of %d is %s\n", i, factors.apply(i));
        }
    }
    
    // 拡張スタイルのモナド則
    // (return x) >>= f ≡ f x
    @Test
    public void testRule1() {
        assertEquals(
            e_bind(e_unit(195955200000000L), factors),
            factors.apply(195955200000000L)
        );
    }
    
    // m >>= return ≡ m
    @Test
    public void testRule2() {
        Enum<Integer> m = new Enum<Integer>(0, 1, -3, 7, 2);
        assertEquals(
            e_bind(m, Enum.<Integer>e_unit()),
            m
        );
    }
    // (m >>= f) >>= g ≡ m >>= ( \x -> (f x >>= g) )
    @Test
    public void testRule3() {
        Enum<Long> m = new Enum<Long>(81L, 82L, 83L, 84L, 85L);
        assertEquals(
            e_bind(e_bind(m, factors), sum1),
            e_bind(m, new Function<Long, Enum<Long>>() {
                public Enum<Long> apply(Long l) {
                    return e_bind(factors.apply(l), sum1);
                }
            })
        );
    }

}
