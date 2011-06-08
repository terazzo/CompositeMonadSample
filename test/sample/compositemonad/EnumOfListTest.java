package sample.compositemonad;

import static sample.compositemonad.Enum.e_map;
import static sample.compositemonad.EnumOfList.el_map;
import static sample.compositemonad.EnumOfList.el_flatten;
import static sample.compositemonad.EnumOfList.el_unit;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Function;


@SuppressWarnings("unchecked")
public class EnumOfListTest {
    
    @Test
    public void testFlatten() {
        EnumOfList<EnumOfList<Integer>> elel = new EnumOfList<EnumOfList<Integer>>(
    		new List<EnumOfList<Integer>>(
        		new EnumOfList<Integer>(		// {[0], [1, -3, 7], [1, 2]}
		            new List<Integer>(0),
		            new List<Integer>(1, -3, 7),
		            new List<Integer>(1, 2)
		        ),
        		new EnumOfList<Integer>(		//	{[0], [1, 2]}
		            new List<Integer>(0),
		            new List<Integer>(1, 2)
		        )
	        ),
    		new List<EnumOfList<Integer>>(
        		new EnumOfList<Integer>(		// {[0], [1], [2]}
		            new List<Integer>(0),
		            new List<Integer>(1),
		            new List<Integer>(2)
		        ),
        		new EnumOfList<Integer>(		//  {[1], [2]}
		            new List<Integer>(1),
		            new List<Integer>(2)
		        )
	        )
        );
        System.out.println("elel = " + elel);
        EnumOfList<Integer>el = el_flatten(elel);
        System.out.println("el = " + el);

		EnumOfList<Integer> expected = new EnumOfList<Integer>(
			// {[0], [1, -3, 7], [1, 2]} x {[0], [1, 2]}
				// [0] x {[0], [1, 2]}
	            new List<Integer>(0,		0),
	            new List<Integer>(0,		1, 2),
				// [1, -3, 7] x {[0], [1, 2]}
	            new List<Integer>(1, -3, 7,	0),
	            new List<Integer>(1, -3, 7,	1, 2),
				// [1, 2] x {[0], [1, 2]}
	            new List<Integer>(1, 2,		0),
	            new List<Integer>(1, 2,		1, 2),
			//  {[0], [1], [2]} x {[1], [2]}
	            // [0] x {[1], [2]}
	            new List<Integer>(0,		1),
	            new List<Integer>(0,		2),
	            // [1] x {[1], [2]}
	            new List<Integer>(1,		1),
	            new List<Integer>(1,		2),
	            // [2] x {[1], [2]}
	            new List<Integer>(2,		1),
	            new List<Integer>(2,		2)
        );
		assertEquals(expected, el);
    }
    @Test
    public void testMap() {
    	EnumOfList<Integer> el1 = new EnumOfList<Integer>(
                new List<Integer>(0),
                new List<Integer>(1, -3, 7),
                new List<Integer>(1, 2)
		);
        System.out.println("el1 = " + el1);
        Function<Integer, Integer> add1 = new Function<Integer, Integer>() {
            public Integer apply(Integer x) {
                return Integer.valueOf(x.intValue() + 1);
            }
        };
        EnumOfList<Integer> el2 = el_map(add1, el1);
        System.out.println("el2 = " + el2);
        EnumOfList<Integer> expected = new EnumOfList<Integer>(
                new List<Integer>(1),
                new List<Integer>(2, -2, 8),
                new List<Integer>(2, 3)
		);
		assertEquals(expected, el2);
    }
    @Test
    public void testUnit() {
		assertEquals(new EnumOfList<Integer>(new List<Integer>(1)), el_unit(1));
		assertEquals(new EnumOfList<String>(new List<String>("hoge")), el_unit("hoge"));
    }

}
