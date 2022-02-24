package com.driga.jstatscore.repository;

import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class AttributeRepository implements Repository<String, Attribute> {

    private static AttributeRepository formRepository;
    private Map<String, Attribute> formMap;

    public AttributeRepository() {
        formMap = new HashMap<String, Attribute>();
    }

    public static AttributeRepository getInstance() {
        if (AttributeRepository.formRepository == null) {
            AttributeRepository.formRepository = new AttributeRepository();
        }
        return AttributeRepository.formRepository;
    }

    @Override
    public Map<String, Attribute> getMap() {
        return formMap;
    }

    @Override
    public void put(String key, Attribute value) {
        formMap.put(key, value);
    }

    @Override
    public void remove(String key, Attribute value) {
        formMap.remove(key, value);
    }

    @Override
    public Attribute find(String key) {
        return formMap.get(key);
    }
}
