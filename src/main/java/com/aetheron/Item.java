package com.aetheron;

public class Item implements IThing {

    private final String description;
    private final String name;

    public Item(String name, String description) {
        this.description = description;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
