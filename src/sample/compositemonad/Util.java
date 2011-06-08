package sample.compositemonad;

import static sample.compositemonad.Enum.e_flatten;
import static sample.compositemonad.Enum.e_unit;
import static sample.compositemonad.Enum.e_map;

import com.google.common.base.Function;
public final class Util {
    public static <X, E extends Enum<X>> Enum<List<X>> combinations(List<E> le) {
        return combinations(new List<X>(), le);
    }
    private static <X, E extends Enum<X>>
            Enum<List<X>> combinations(final List<X> path, List<E> le) {

        if (le.isEmpty()) {
            return e_unit(path);
        } else {
            Enum<X> head = le.head();
            final List<E> tail = le.tail();

            return e_flatten(e_map(new Function<X, Enum<List<X>>>() {
                public Enum<List<X>> apply(X x) {
                    return combinations(path.appended(x), tail);
                }
            }, head));
        }
    }
    public static <X, E extends Enum<X>> Function<List<E>, Enum<List<X>>> combinations() {
        return new Function<List<E>, Enum<List<X>>>() {
            public Enum<List<X>> apply(List<E> le) {
                return combinations(le);
            }
        };
    }
   
}
