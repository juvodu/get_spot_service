package com.juvodu.database.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Country wrapper, containing the ISO 2 letter code and a verbose name
 *
 * @author Juvodu
 */
public class Country implements Comparable<Country> {

    /**
     * 2 letter ISO country code
     */
    private String code;

    /**
     * name of the country
     */
    private String name;

    @JsonCreator
    public Country(@JsonProperty("code") String code, @JsonProperty("name") String name) {
        super();
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Country o) {
        return this.name.compareTo(o.name);
    }
}
