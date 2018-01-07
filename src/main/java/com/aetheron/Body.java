package com.aetheron;

import java.util.*;

public class Body {

    public final Map<String, String> stringProps = new HashMap<>();

    public final List<Item> items = new ArrayList<>();

    private final Conn conn;

    public Body(Conn conn) {
        this.conn = conn;
    }

    public void add(Item item) {
        items.add(item);
    }

    public void remove(Item item) {
        items.remove(item);
    }

    public Optional<Item> getItem(String test) {
        return items.stream().filter(item -> item.getName().contains(test)).findFirst();
    }
    public void sendOutput(String msg) {
        conn.sendOutput(msg);
    }

    public String pollInput() {
        final String input = conn.pollInput();
        return input;
    }
}
