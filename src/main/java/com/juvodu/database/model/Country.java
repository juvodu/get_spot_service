package com.juvodu.database.model;

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

    public Country(String code, String name) {
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
