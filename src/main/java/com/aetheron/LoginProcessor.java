package com.aetheron;

import java.util.HashSet;
import java.util.Set;

public class LoginProcessor implements IProcessor {

    Set<Body> state = new HashSet<>();
    World world;

    public LoginProcessor(World world) {
        this.world = world;
    }

    public void process(String input, Body body, Environment env, Environment outerEnv) {
        final String name = body.stringProps.get("name");
        final String password = body.stringProps.get("password");
        if (name == null || name.length() < 3) {
            body.stringProps.put("name", input);
            body.sendOutput("What is your password?\n");
        }
        else if (password == null || password.length() < 6) {
            body.stringProps.put("password", input);
            state.remove(body);
            world.add(body);
        }
    }
}
