package com.driga.jstatscore.api;


import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.api.repository.Repository;
import com.driga.jstatscore.database.DatabaseProvider;
import com.driga.jstatscore.repository.AttributeRepository;
import com.driga.jstatscore.repository.FormRepository;
import com.driga.jstatscore.repository.SubjectRepository;

import java.util.UUID;

public class JStatsCoreAPI {

    private static JStatsCoreAPI jStatsCoreAPI;

    public static JStatsCoreAPI getInstance() {
        if (JStatsCoreAPI.jStatsCoreAPI == null) {
            JStatsCoreAPI.jStatsCoreAPI = new JStatsCoreAPI();
        }
        return JStatsCoreAPI.jStatsCoreAPI;
    }

    public Repository<UUID, Subject> getSubjects() {
        return SubjectRepository.getInstance();
    }

    public Repository<String, Form> getForms() {
        return FormRepository.getInstance();
    }

    public Repository<String, Attribute> getAttributes() {
        return AttributeRepository.getInstance();
    }

    public DatabaseProvider getDatabase() {
        return JStatsCore.getInstance().getDatabaseProvider();
    }
}
