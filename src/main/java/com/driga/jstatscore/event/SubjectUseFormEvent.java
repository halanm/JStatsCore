package com.driga.jstatscore.event;

import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

public class SubjectUseFormEvent extends EventWrapper {

    private Subject subject;
    private Form form;

    public SubjectUseFormEvent(Subject subject, Form form){
        this.subject = subject;
        this.form = form;
        Bukkit.getPluginManager().callEvent(this);
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Subject getSubject() {
        return subject;
    }

    public Form getForm() {
        return form;
    }
}
