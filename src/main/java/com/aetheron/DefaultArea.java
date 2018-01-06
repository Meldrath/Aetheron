package com.aetheron;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultArea implements IArea, Environment {

    private final Map<Body, Room> playingBodies = new HashMap<>();
    private final IProcessor processor = new Processor();
    private final Room startRoom = new Room();

    @Override
    public void doTick() {
        playingBodies.forEach((body, room) -> {
            final String input = body.pollInput();
            if (input != null) {
                processor.process(input, body, room, this);
            }
        });
    }

    @Override
    public Collection<Body> getLocalBodies(Body body) {
        return playingBodies.keySet();
    }

    @Override
    public void add(Body body) {
        playingBodies.put(body, startRoom);
        startRoom.add(body);
        body.sendOutput("Welcome.");
    }
}
