package com.aetheron;

import java.util.*;

public class DefaultArea implements IArea, Environment {

    private final IProcessor processor = new Processor();
    private final Room startRoom = new Room();
    private final Set<Room> rooms = new HashSet<>(Arrays.asList(startRoom));

    @Override
    public void doTick() {
        rooms.forEach(room -> room.getLocalBodies().forEach(body -> {
            final String input = body.pollInput();
            if (input != null) {
                processor.process(input, body, room, this);
            }
        }));
    }

    @Override
    public void add(Room room) {
        rooms.add(room);
    }

    @Override
    public String getDescription() {
        return "A nondescript area";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Body> getLocalBodies() {
        return Collections.EMPTY_SET;
    }

    @Override
    public void add(Body body) {
        startRoom.add(body);
        body.sendOutput("Welcome.\n");
    }
}
