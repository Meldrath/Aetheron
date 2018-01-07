package com.aetheron;

import java.util.*;
import java.util.stream.Collectors;

public class Room implements Environment {

    public final Map<String, Exit> exits = new HashMap<>();

    private final String description;
    private final Set<Body> bodies = new HashSet<>();
    private final Set<Item> items = new HashSet<>();

    public Room() {
        this.description = "A nondescript room";
    }

    public Room(String description) {
        this.description = description;
    }

    public void add(Item i) {
        items.add(i);
    }

    public Optional<Item> getItem(String test) {
        return items.stream().filter(item -> item.getName().contains(test)).findFirst();
    }

    public void remove(Item item) {
        items.remove(item);
    }

    public void add(Body b) {
        bodies.forEach(body -> {
            if (!body.equals(b)) {
                body.sendOutput(b.stringProps.get("name") + " appears.\n");
            }
        });
        bodies.add(b);
        displayTo(b);
    }

    public void moveTo(Body body, Room other) {
        bodies.remove(body);
        other.add(body);
    }

    public void displayTo(Body body) {
        body.sendOutput(getDescription() + "\n");
        if (exits.isEmpty()) {
            body.sendOutput("Exits: none.\n");
        }
        else {
            body.sendOutput("Exits: " + exits.keySet().stream().collect(Collectors.joining(", ")) + ".\n");
        }

        if (!items.isEmpty()) {
            body.sendOutput("You see laying here:\n");
            items.forEach(item -> body.sendOutput(item.getName() + "\n"));
        }

        if (bodies.size() < 2) {
            body.sendOutput("You see no one here.\n");
        } else {
            body.sendOutput("You see here:\n");
            bodies.forEach(otherBody -> body.sendOutput(otherBody.stringProps.get("name") + "\n"));
        }
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Collection<Body> getLocalBodies() {
        return bodies;
    }
}
