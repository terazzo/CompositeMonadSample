package sample.compositemonad;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Function;

public final class Util {
    private static <X, E extends Enum<X>> Set<List<X>> choice(List<X> path, List<E> rest) {
        Set<List<X>> result = new HashSet<List<X>>();
        if (rest.isEmpty()) {
            result.add(path);
        } else {
            E head = rest.head();
            List<E> tail = rest.tail();
            for (X x : head.components) {
                result.addAll(choice(path.appended(x), tail));
            }
        }
        return result;
    }
    public static <X, E extends Enum<X>> Enum<List<X>> combinations(List<E> le) {
        return new Enum<List<X>>(choice(new List<X>(), le));
    }
    public static <X, E extends Enum<X>> Function<List<E>, Enum<List<X>>> combinations() {
        return new Function<List<E>, Enum<List<X>>>() {
            public Enum<List<X>> apply(List<E> le) {
                return combinations(le);
            }
        };
    }
   
}
