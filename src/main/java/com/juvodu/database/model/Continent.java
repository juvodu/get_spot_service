package com.juvodu.database.model;

/**
 * Enum defining possible continent values
 *
 * @author Juvodu
 */
public enum Continent {

    AF("Africa", "AF"),
    NA("North America", "NA"),
    OC("Oceania", "OC"),
    AN("Antarctica", "AN"),
    AS("Asia", "AS"),
    EU("Europe", "EU"),
    SA("South America", "SA");

    private final String name;
    private final String code;

    Continent(String name, String code){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}