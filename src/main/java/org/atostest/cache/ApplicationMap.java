package org.atostest.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApplicationMap<K, V> extends LinkedHashMap {

    private static final int MAX_SIZE = 50;

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_SIZE;
    }
}
