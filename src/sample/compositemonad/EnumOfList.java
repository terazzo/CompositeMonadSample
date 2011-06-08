package sample.compositemonad;

import static sample.compositemonad.List.l_map;
import static sample.compositemonad.List.l_unit;

import java.util.Set;

import com.google.common.base.Function;

public class EnumOfList<X> extends Enum<List<X>> {
    public EnumOfList(Set<List<X>> components) {
        super(components);
    }
    public EnumOfList(List<X>... components) {
        super(components);
    }
    public EnumOfList(Enum<List<X>> el) {
        this(el.components);
    }





    public static <X, Y> EnumOfList<Y> el_map(Function<X, Y> f, EnumOfList<X> el) {
        return new EnumOfList<Y>(e_map(l_map(f), el));
    }
    public static <X, Y> Function<EnumOfList<X>, EnumOfList<Y>> el_map(final Function<X, Y> f) {
        return new Function<EnumOfList<X>, EnumOfList<Y>>() {
            public EnumOfList<Y> apply(EnumOfList<X> el) {
                return el_map(f, el);
            }
        };
    }
//    public static <X> EnumOfList<X> el_flattenX(EnumOfList<EnumOfList<X>> elel) {
//        Function<List<EnumOfList<X>>, Enum<List<List<X>>>> combinations = Util.<List<X>,EnumOfList<X>>combinations();
//        Enum<Enum<List<List<X>>>> eell = Enum.<List<EnumOfList<X>>, Enum<List<List<X>>>>e_map(combinations, elel);
//        Function<Enum<List<List<X>>>, Enum<List<X>>> fell_el = Enum.<List<List<X>>,List<X>>e_map(List.<X>l_flatten());
//        Enum<Enum<List<X>>> eel = Enum.<Enum<List<List<X>>>, Enum<List<X>>>e_map(
//                fell_el, eell);
//        Enum<List<X>> el = Enum.<List<X>>e_flatten(eel);
//        return new EnumOfList<X>(el);
//    }
    public static <X> EnumOfList<X> el_flatten(EnumOfList<EnumOfList<X>> elel) {
      return new EnumOfList<X>(
              e_flatten(
                  e_map(
                      e_map(List.<X>l_flatten()), 
                      e_map(Util.<List<X>,EnumOfList<X>>combinations(), elel)
      )));
    }
    public static <X> Function<EnumOfList<EnumOfList<X>>, EnumOfList<X>> el_flatten() {
        return new Function<EnumOfList<EnumOfList<X>>, EnumOfList<X>>() {
            public EnumOfList<X> apply(EnumOfList<EnumOfList<X>> el) {
                return el_flatten(el);
            }
            
        };
    }
    public static <X> EnumOfList<X> el_unit(X x) {
        return new EnumOfList<X>(e_unit(l_unit(x)));
    }
    public static <X> Function<X, EnumOfList<X>> el_unit() {
        return new Function<X, EnumOfList<X>>() {
            public EnumOfList<X> apply(X x) {
                return el_unit(x);
            }
        };
    }
}
