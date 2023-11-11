package com.mygolfclub.utils.mapping;

import java.util.Collection;

/**
 * @param <T> mapped from
 * @param <S> mapped to
 */
public interface OneWayMappable<T, S> {
    S mapTo(T t);

    default Collection<S> mapTo(Collection<T> ts) {
        return ts.stream()
                .map(this::mapTo)
                .toList();
    }
}
