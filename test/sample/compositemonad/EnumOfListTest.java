package sample.compositemonad;

import static sample.compositemonad.EnumOfList.el_flatten;
import static sample.compositemonad.EnumOfList.el_unit;
import static org.junit.Assert.*;

import org.junit.Test;


@SuppressWarnings("unchecked")
public class EnumOfListTest {
    
    @Test
    public void testFlatten() {
        EnumOfList<Integer> el1 = new EnumOfList<Integer>(
            new List<Integer>(0),
            new List<Integer>(1, -3, 7),
            new List<Integer>(1, 2)
        );
        EnumOfList<EnumOfList<Integer>> elel = el_unit(el1);
        System.out.println("elel = " + elel);
        EnumOfList<Integer>el = el_flatten(elel);
        System.out.println("el = " + el);
		assertEquals(el1, el);
    }

}
