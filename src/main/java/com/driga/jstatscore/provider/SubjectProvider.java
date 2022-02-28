package com.driga.jstatscore.provider;

import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.form.FormManager;
import com.driga.jstatscore.repository.AttributeRepository;

import java.util.HashMap;
import java.util.Map;

public class SubjectProvider {

    private static SubjectProvider subjectProvider;

    public static SubjectProvider getInstance() {
        if (SubjectProvider.subjectProvider == null) {
            SubjectProvider.subjectProvider = new SubjectProvider();
        }
        return SubjectProvider.subjectProvider;
    }

    public double getAttributeValue(Subject subject, String attr){
        double level = subject.getAttributeLevel(attr);
        Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find(attr);
        double multiplier = attribute.getMultiplier();
        if(FormManager.getInstance().isUsing(subject)){
            Form form = FormManager.getInstance().getUsingForm(subject);
            multiplier += form.getAttributeMultiplier(attribute);
        }
        double value = level * multiplier;
        return value;
    }

    public double getSpeedValue(Subject subject){
        double level = subject.getAttributeLevel("STRENGTH");
        Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("STRENGTH");
        double max = attribute.getMaxValue();
        double speed = 0.2;
        double toAdd = rot(max, 0.8, level);
        return speed + toAdd;
    }

    public Map<String, Double> fromString(String string) {
        Map<String, Double> map = new HashMap<>();
        if (string != null) {
            String[] pairs = string.replace("{", "").replace("}", "").replaceAll(" ", "").split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                map.put(keyValue[0], Double.valueOf(keyValue[1]));
            }
        }
        return map;
    }

    public Map<String, Double> defaultMap(){
        Map<String, Double> map = new HashMap<>();
        for(String attr : AttributeRepository.getInstance().getMap().keySet()){
            map.put(attr, 1.0);
        }
        map.put("HP", 1.0 * JStatsCoreAPI.getInstance().getAttributes().find("CONSTITUTION").getMultiplier());
        map.put("SP", 1.0 * JStatsCoreAPI.getInstance().getAttributes().find("ENERGY").getMultiplier());
        return map;
    }

    private Double rot(Double var1, Double var2, Double var3){
        double p0 = var2 * var3;
        double p1 = p0 / var1;
        return p1;
    }
}
