package com.aetheron;

import java.util.*;

class Room implements Environment {
    private final Set<Body> bodies = new HashSet<>();
    private final Map<String, Room> neighboringRooms = new HashMap<>();

    public void add(Body b) {
        bodies.forEach(body -> body.sendOutput(b.stringProps.get("name") + " appears.\n"));
        bodies.add(b);
    }

    @Override
    public Collection<Body> getLocalBodies(Body body) {
        return bodies;
    }
}
