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
    // �v�f���󂩂ǂ����𔻒�
    public boolean isEmpty() {
        return components.isEmpty();
    }
    // �擪�̗v�f��߂�
    public T head() {
        if (isEmpty()) {
            throw new IllegalStateException("empty list has no head.");
        }
        return components.get(0);
    }
    // �擪�̗v�f����菜�����c���List�Ŗ߂�
    public List<T> tail() {
        if (isEmpty()) {
            throw new IllegalStateException("empty list has no tail.");
        }
        return new List<T>(components.subList(1, components.size()));
    }
    // ������x��t��������List��߂�
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





    // extension style
    public static <X, Y> List<Y> l_bind(List<X> l, Function<X, List<Y>> f) {
        return l_flatten(l_map(f, l));
    }

    // Function version
    public static <X, Y> Function<List<X>, List<Y>> l_bind(final Function<X, List<Y>> f) {
        return new Function<List<X>, List<Y>>() {
            public List<Y> apply(List<X> l) {
                return l_bind(l, f);
            }
        };
    }
    // map��unit��bind�ŏ����Ă݂�
    public static <X, Y> List<Y> l_map_by_ext(final Function<X, Y> f, List<X> l) {
        return l_bind(l, new Function<X, List<Y>>() {
            public List<Y> apply(X x) {
                return l_unit(f.apply(x));
            }
        });
    }
    // flatten��unit��bind�ŏ����Ă݂�
    public static <X> List<X> l_flatten_by_ext(List<List<X>> ll) {
        return l_bind(ll, l_bind(List.<X>l_unit()));
    }
}
