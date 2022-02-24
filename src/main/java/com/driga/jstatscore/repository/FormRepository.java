package com.driga.jstatscore.repository;

import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class FormRepository implements Repository<String, Form> {

    private static FormRepository formRepository;
    private Map<String, Form> formMap;

    public FormRepository() {
        formMap = new HashMap<String, Form>();
    }

    public static FormRepository getInstance() {
        if (FormRepository.formRepository == null) {
            FormRepository.formRepository = new FormRepository();
        }
        return FormRepository.formRepository;
    }

    @Override
    public Map<String, Form> getMap() {
        return formMap;
    }

    @Override
    public void put(String key, Form value) {
        formMap.put(key, value);
    }

    @Override
    public void remove(String key, Form value) {
        formMap.remove(key, value);
    }

    @Override
    public Form find(String key) {
        return formMap.get(key);
    }
}
