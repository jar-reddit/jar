package com.example.JAR.RAW;

import java.util.List;

/**
 * Reddit Data Type used to paginate content
 */
public class Listing {
    private String before; //
    private String after;
    private String modhash;
    private List<Thing> children; // TODO: 15/10/20 Is List the best DataType for this?
}
