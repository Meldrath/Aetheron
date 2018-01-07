package com.aetheron;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Equipment extends Item {

    private final Set<String> locations;

    @SuppressWarnings("unchecked")
    public Equipment(String name, String description, String... loc) {
        super(name, description);
        locations = new HashSet(Arrays.asList(loc));
    }

    public String getLocation() {
        return locations.iterator().next();
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "locations=" + locations +
                '}';
    }
}
