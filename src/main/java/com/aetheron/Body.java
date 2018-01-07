package com.aetheron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Body {

    public final Map<String, String> stringProps = new HashMap<>();

    public final List<IThing> items = new ArrayList<>();

    private final Conn conn;

    public Body(Conn conn) {
        this.conn = conn;
    }

    public void sendOutput(String msg) {
        conn.sendOutput(msg);
    }

    public String pollInput() {
        final String input = conn.pollInput();
        return input;
    }
}
