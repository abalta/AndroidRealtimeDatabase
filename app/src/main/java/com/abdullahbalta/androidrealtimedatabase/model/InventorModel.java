package com.abdullahbalta.androidrealtimedatabase.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Abdullah on 2.10.2016.
 */

public class InventorModel implements Serializable {

    private int inventor_id;
    private String inventor_name;
    private String image;
    private String wiki;
    private List<String> profession;

    public InventorModel() {
    }

    public void setInventor_id(int inventor_id) {
        this.inventor_id = inventor_id;
    }

    public void setInventor_name(String inventor_name) {
        this.inventor_name = inventor_name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public void setProfession(List<String> profession) {
        this.profession = profession;
    }

    public int getInventor_id() {
        return inventor_id;
    }

    public String getInventor_name() {
        return inventor_name;
    }

    public String getImage() {
        return image;
    }

    public List<String> getProfession() {
        return profession;
    }

    public String getWiki() {
        return wiki;
    }

    public String join(List<String> list, String delim) {

        StringBuilder sb = new StringBuilder();

        String loopDelim = "";

        for(String s : list) {

            sb.append(loopDelim);
            sb.append(s);

            loopDelim = delim;
        }

        return sb.toString();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("inventor_id", inventor_id);
        result.put("inventor_name", inventor_name);
        result.put("image", image);
        result.put("profession", profession);
        result.put("wiki", wiki);
        return result;
    }
}
