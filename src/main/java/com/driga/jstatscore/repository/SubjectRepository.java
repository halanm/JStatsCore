package com.driga.jstatscore.repository;

import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.api.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SubjectRepository implements Repository<UUID, Subject> {

    private static SubjectRepository subjectRepository;
    private Map<UUID, Subject> subjectMap;

    public SubjectRepository() {
        subjectMap = new HashMap<UUID, Subject>();
    }

    public static SubjectRepository getInstance() {
        if (SubjectRepository.subjectRepository == null) {
            SubjectRepository.subjectRepository = new SubjectRepository();
        }
        return SubjectRepository.subjectRepository;
    }

    @Override
    public Map<UUID, Subject> getMap() {
        return subjectMap;
    }

    @Override
    public void put(UUID key, Subject value) {
        subjectMap.put(key, value);
    }

    @Override
    public void remove(UUID key, Subject value) {
        subjectMap.remove(key, value);
    }

    @Override
    public Subject find(UUID key) {
        return subjectMap.get(key);
    }
}
