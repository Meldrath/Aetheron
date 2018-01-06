package com.aetheron;

import java.util.Collection;

public class Processor implements IProcessor {

    public void process(String input, Body body, Environment env, Environment outerEnv) {
        if (input == null || "".equals(input)) {
            return;
        }

        if (input.startsWith("say ")) {
            env.getLocalBodies(body).forEach(otherBody -> {
                if (otherBody.equals(body)) {
                    otherBody.sendOutput("You say, \"" + input + "\"\n");
                }
                else {
                    otherBody.sendOutput(body.stringProps.get("name") + " says, \"" + input + "\"\n");
                }
            });
        }
        else if (input.equals("look")) {
            Collection<Body> bodies = env.getLocalBodies(body);
            if (bodies.size() < 2) {
                body.sendOutput("You see nothing here.\n");
            } else {
                body.sendOutput("You see here:\n");
                bodies.forEach(otherBody -> body.sendOutput(otherBody.stringProps.get("name") + "\n"));
            }
        }
        else {
            body.sendOutput("I don't understand, \"" + input + "\"\n");
        }
    }
}
