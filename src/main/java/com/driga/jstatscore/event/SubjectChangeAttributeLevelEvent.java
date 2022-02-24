package com.driga.jstatscore.event;

import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

public class SubjectChangeAttributeLevelEvent extends EventWrapper {

    private Subject subject;
    private Attribute attribute;
    private Double level;

    public SubjectChangeAttributeLevelEvent(Subject subject, Attribute attribute, Double level){
        this.subject = subject;
        this.attribute = attribute;
        this.level = level;
        Bukkit.getPluginManager().callEvent(this);
    }


    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Subject getSubject() {
        return subject;
    }

    public Double getLevel() {
        return level;
    }
}
