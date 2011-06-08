package sample.compositemonad;

import static sample.compositemonad.Enum.e_flatten;
import static sample.compositemonad.Enum.e_unit;
import static sample.compositemonad.Enum.e_map;

import com.google.common.base.Function;
public final class Util {
    private static <X, E extends Enum<X>> Enum<List<X>> choice(final List<X> path, List<E> rest) {
        if (rest.isEmpty()) {
            return e_unit(path);
        } else {
            E head = rest.head();
            final List<E> tail = rest.tail();

            return e_flatten(e_map(new Function<X, Enum<List<X>>>() {
                public Enum<List<X>> apply(X x) {
                    return choice(path.appended(x), tail);
                }
            }, head));
        }
    }
    public static <X, E extends Enum<X>> Enum<List<X>> combinations(List<E> le) {
        return choice(new List<X>(), le);
    }
    public static <X, E extends Enum<X>> Function<List<E>, Enum<List<X>>> combinations() {
        return new Function<List<E>, Enum<List<X>>>() {
            public Enum<List<X>> apply(List<E> le) {
                return combinations(le);
            }
        };
    }
   
}
