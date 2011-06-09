package sample.compositemonad;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.base.Function;

public class Enum<T> {
    private final Set<T> components;

    public Enum(Collection<T> components) {
        if (components == null) {
            throw new IllegalArgumentException("the arg should not be null");
        }
        this.components = Collections.unmodifiableSet(new HashSet<T>(components));
    }
    public Enum(T... components) {
        this(Arrays.asList(components));
    }
    private static <A> A nullChecked(A o) throws IllegalArgumentException {
        if (o == null) {
            throw new IllegalArgumentException("the arg should not be null");
        }
        return o;
    }
    public Enum(Enum<T> other) {
        this(nullChecked(other.components));
    }
    public final boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Iterator<T> it = components.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) sb.append(',').append(' ');
        }
        sb.append('}');
        return sb.toString();
    }

    public static <X, Y> Enum<Y> e_map(Function<X, Y> f, Enum<X> e) {
        HashSet<Y> results = new HashSet<Y>();
        for (X x : e.components) {
            results.add(f.apply(x));
        }
        return new Enum<Y>(results);
    }
    public static <X> Enum<X> e_flatten(Enum<Enum<X>> ee) {
        HashSet<X> results = new HashSet<X>();
        for (Enum<X> e : ee.components) {
            results.addAll(e.components);
        }
        return new Enum<X>(results);
    }
    public static <X> Enum<X> e_unit(X x) {
        HashSet<X> results = new HashSet<X>();
        results.add(x);
        return new Enum<X>(results);
    }

    // Function version
    public static <X, Y> Function<Enum<X>, Enum<Y>> e_map(final Function<X, Y> f) {
        return new Function<Enum<X>, Enum<Y>>() {
            public Enum<Y> apply(Enum<X> l) {
                return e_map(f, l);
            }
        };
    }
    public static <X> Function<Enum<Enum<X>>, Enum<X>> e_flatten() {
        return new Function<Enum<Enum<X>>, Enum<X>>() {
            public Enum<X> apply(Enum<Enum<X>> ee) {
                return e_flatten(ee);
            }
        };
    }
    public static <X> Function<X, Enum<X>> e_unit() {
        return new Function<X, Enum<X>>() {
            public Enum<X> apply(X x) {
                return e_unit(x);
            }
        };
    }

    // extension style
    public static <X, Y> Enum<Y> e_bind(Enum<X> e, Function<X, Enum<Y>> f) {
        return e_flatten(e_map(f, e));
     }
 

}
