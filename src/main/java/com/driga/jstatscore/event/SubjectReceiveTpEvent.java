package com.driga.jstatscore.event;

import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

public class SubjectReceiveTpEvent extends EventWrapper {

    private Subject subject;
    private Double value;

    public SubjectReceiveTpEvent(Subject subject, Double value){
        this.subject = subject;
        this.value = value;
        Bukkit.getPluginManager().callEvent(this);
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Subject getSubject() {
        return subject;
    }

    public Double getValue() {
        return value;
    }
}
