package com.driga.jstatscore.api.repository;

import java.util.Map;

public interface Repository<K, V>{

    Map<K, V> getMap();

    void put(K p0, V p1);

    void remove(K p0, V p1);

    V find(K p0);
}
