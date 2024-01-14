package com.cerbon.cerbons_api.api.general.data;

import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class represents a weak hash predicate that tests objects of a generic type.
 * It uses a weak hash map to store the conditions for each object and a supplier to generate these conditions.
 * The conditions are weakly referenced, meaning they can be garbage collected when they are no longer in ordinary use.
 *
 * @param <T> the type of objects that this predicate can test
 */
public class WeakHashPredicate<T> implements Predicate<T> {
    private final Supplier<Supplier<Boolean>> predicateFactory;
    private final WeakHashMap<T, Supplier<Boolean>> conditionals = new WeakHashMap<>();

    public WeakHashPredicate(Supplier<Supplier<Boolean>> predicateFactory) {
        this.predicateFactory = predicateFactory;
    }

    @Override
    public boolean test(T t) {
        if (!conditionals.containsKey(t))
            conditionals.put(t, predicateFactory.get());

        return conditionals.get(t).get();
    }
}
