package sample.compositemonad;

import static sample.compositemonad.List.l_bind;
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
        super(el);
    }





    // el_map(f, el) := e_map(l_map(f), el)
    public static <X, Y> EnumOfList<Y> el_map(Function<X, Y> f, EnumOfList<X> el) {
        return new EnumOfList<Y>(e_map(l_map(f), el));
    }
    // el_flatten(elel) := e_flatten(e_map(e_map(l_flatten), e_map(combinations, elel)))
    public static <X> EnumOfList<X> el_flatten(EnumOfList<EnumOfList<X>> elel) {
        return new EnumOfList<X>(
              e_flatten(
                  e_map(
                      e_map(List.<X>l_flatten()), 
                      e_map(Util.<List<X>, EnumOfList<X>>combinations(), elel)
        )));
    }
    // el_unit(x) := e_unit(l_unit(x))
    public static <X> EnumOfList<X> el_unit(X x) {
        return new EnumOfList<X>(e_unit(l_unit(x)));
    }

    // Function version
    public static <X, Y> Function<EnumOfList<X>, EnumOfList<Y>> el_map(final Function<X, Y> f) {
        return new Function<EnumOfList<X>, EnumOfList<Y>>() {
            public EnumOfList<Y> apply(EnumOfList<X> el) {
                return el_map(f, el);
            }
        };
    }
    public static <X> Function<X, EnumOfList<X>> el_unit() {
        return new Function<X, EnumOfList<X>>() {
            public EnumOfList<X> apply(X x) {
                return el_unit(x);
            }
        };
    }
    public static <X> Function<EnumOfList<EnumOfList<X>>, EnumOfList<X>> el_flatten() {
        return new Function<EnumOfList<EnumOfList<X>>, EnumOfList<X>>() {
            public EnumOfList<X> apply(EnumOfList<EnumOfList<X>> el) {
                return el_flatten(el);
            }
        };
    }

    // extension style
    public static <X, Y> EnumOfList<Y> el_bind(EnumOfList<X> el, Function<X, EnumOfList<Y>> f) {
        return el_flatten(el_map(f, el));
    }


    // full extension style

    // el_map(f, el) := e_map(l_map(f), el)
    public static <X, Y> EnumOfList<Y> el_map_by_ext(final Function<X, Y> f, EnumOfList<X> el) {
        return new EnumOfList<Y>(
            e_bind(el, new Function<List<X>, Enum<List<Y>>>() {
                public Enum<List<Y>> apply(List<X> x) {
                    return e_unit(l_bind(x, new Function<X, List<Y>>() {
                                public List<Y> apply(X x) {
                                    return l_unit(f.apply(x));
                                }
                            }));
                }
            })
         );
    }

    // el_flatten(elel) := e_flatten(e_map(e_map(l_flatten), e_map(combinations, elel)))
    public static <X> EnumOfList<X> el_flatten_by_ext(EnumOfList<EnumOfList<X>> elel) {
        return new EnumOfList<X>(
              e_bind(
                  e_bind(elel, new Function<List<EnumOfList<X>>, Enum<Enum<List<List<X>>>>>() {
                      public Enum<Enum<List<List<X>>>> apply(List<EnumOfList<X>> x) {
                          return e_unit(Util.<List<X>, EnumOfList<X>>combinations().apply(x));
                      }
                  }),
                  e_bind(new Function<List<List<X>>, Enum<List<X>>>() {
                      public Enum<List<X>> apply(List<List<X>> x) {
                          return e_unit(l_bind(x, l_bind(List.<X>l_unit())));
                      }
                  }))
                      
        );
    }

    public static <X, Y> EnumOfList<Y> el_bind_by_ext(EnumOfList<X> el, final Function<X, EnumOfList<Y>> f) {
       return new EnumOfList<Y>(
            e_bind(
                e_bind(
                    e_bind(el, new Function<List<X>, Enum<List<EnumOfList<Y>>>>() {
                        public Enum<List<EnumOfList<Y>>> apply(List<X> x) {
                            return e_unit(l_bind(x, new Function<X, List<EnumOfList<Y>>>() {
                                public List<EnumOfList<Y>> apply(X x) {
                                    return l_unit(f.apply(x));
                                }
                            }));
                        }
                    }),
                    new Function<List<EnumOfList<Y>>, Enum<Enum<List<List<Y>>>>>() {
                        public Enum<Enum<List<List<Y>>>> apply(List<EnumOfList<Y>> l) {
                            return e_unit(Util.<List<Y>, EnumOfList<Y>>combinations().apply(l));
                        }
                    }),
                e_bind(new Function<List<List<Y>>, Enum<List<Y>>>() {
                    public Enum<List<Y>> apply(List<List<Y>> x) {
                        return e_unit(l_bind(x, l_bind(List.<Y>l_unit())));
                    }
                })
          ));
    }
}
