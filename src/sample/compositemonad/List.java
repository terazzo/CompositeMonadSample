package sample.compositemonad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.base.Function;

public class List<T> {
    private final java.util.List<T> components;

    public List(java.util.List<T> components) {
        if (components == null) {
            throw new IllegalArgumentException("the arg should not be null");
        }
        this.components = Collections.unmodifiableList(components);
    }
    public List(T... components) {
        this(Arrays.asList(components));
    }
    public final boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Iterator<T> it = components.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) sb.append(',').append(' ');
        }
        sb.append(']');
        return sb.toString();
    }
    // —v‘f‚ª‹ó‚©‚Ç‚¤‚©‚ğ”»’è
    public boolean isEmpty() {
        return components.isEmpty();
    }
    // æ“ª‚Ì—v‘f‚ğ–ß‚·
    public T head() {
        if (isEmpty()) {
            throw new IllegalStateException("empty list has no head.");
        }
        return components.get(0);
    }
    // æ“ª‚Ì—v‘f‚ğæ‚èœ‚¢‚½c‚è‚ğList‚Å–ß‚·
    public List<T> tail() {
        if (isEmpty()) {
            throw new IllegalStateException("empty list has no tail.");
        }
        return new List<T>(components.subList(1, components.size()));
    }
    // ––”ö‚Éx‚ğ•t‚¯‰Á‚¦‚½List‚ğ–ß‚·
    public List<T> appended(T x) {
        ArrayList<T> results = new ArrayList<T>(components);
        results.add(x);
        return new List<T>(results);
    }

    public static <X, Y> List<Y> l_map(Function<X, Y> f, List<X> l) {
        ArrayList<Y> results = new ArrayList<Y>();
        for (X x : l.components) {
            results.add(f.apply(x));
        }
        return new List<Y>(results);
    }
    public static <X> List<X> l_flatten(List<List<X>> ll) {
        ArrayList<X> results = new ArrayList<X>();
        for (List<X> l : ll.components) {
            results.addAll(l.components);
        }
        return new List<X>(results);
    }
    public static <X> List<X> l_unit(X x) {
        ArrayList<X> results = new ArrayList<X>();
        results.add(x);
        return new List<X>(results);
    }

    // Function version
    public static <X, Y> Function<List<X>, List<Y>> l_map(final Function<X, Y> f) {
        return new Function<List<X>, List<Y>>() {
            public List<Y> apply(List<X> l) {
                return l_map(f, l);
            }
        };
    }
    public static <X> Function<List<List<X>>, List<X>> l_flatten() {
        return new Function<List<List<X>>, List<X>>() {
            public List<X> apply(List<List<X>> ll) {
                return l_flatten(ll);
            }
        };
    }
    public static <X> Function<X, List<X>> l_unit() {
        return new Function<X, List<X>>() {
            public List<X> apply(X x) {
                return l_unit(x);
            }
        };
    }
}
