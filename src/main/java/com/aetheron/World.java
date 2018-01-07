package com.aetheron;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class World implements Environment {

    private final ConcurrentHashMap.KeySetView<Body, Boolean> newBodies = ConcurrentHashMap.newKeySet();
    private final IProcessor loginProcessor = new LoginProcessor(this);
    private final IArea defaultArea = new DefaultArea();
    private final List<IArea> areas = new ArrayList<>();
    {
        areas.add(defaultArea);
    }

    public void add(Conn conn) {
        Body b = new Body(conn);
        newBodies.add(b);
        b.sendOutput("What is your name?\n");
    }

    public void add(Body b) {
        newBodies.remove(b);
        defaultArea.add(b);
    }

    public void doTick() {
        newBodies.forEach(body -> {
            final String input = body.pollInput();
            if (input != null && !"".equals(input)) {
                loginProcessor.process(input, body, null, null);
            }
        });
        areas.forEach(IArea::doTick);
    }

    @Override
    public String getDescription() {
        return "A nondescript world";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Body> getLocalBodies() {
        return Collections.EMPTY_LIST;
    }
}
