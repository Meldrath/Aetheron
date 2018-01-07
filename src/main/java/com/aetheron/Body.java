package com.aetheron;

import java.util.*;

public class Body {

    public final Map<String, String> stringProps = new HashMap<>();

    public final List<Item> items = new ArrayList<>();
    public final Map<String, Equipment> equipment = new HashMap<>();

    private final Conn conn;

    public Body(Conn conn) {
        this.conn = conn;
    }

    public Map<String, Equipment> getEquipment() {
        return equipment;
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

    public Optional<Equipment> getEquipmentItem(String test) {
        return items.stream().filter(item -> item.getName().contains(test) && item instanceof Equipment).map(item -> (Equipment) item).findFirst();
    }

    public void add(String loc, Equipment eq) {
        equipment.put(loc, eq);
    }

    public void remove(Equipment eq) {
        for (Map.Entry entry : equipment.entrySet()) {
            if (eq.equals(entry.getValue())) {
                equipment.remove(entry.getKey());
                return;
            }
        }
    }

    public Optional<Equipment> getEquipment(String test) {
        System.out.println(test + " : " + equipment);
        return equipment.keySet().stream().filter(item -> item.contains(test)).findFirst().map(name -> equipment.get(name));
    }

    public void sendOutput(String msg) {
        conn.sendOutput(msg);
    }

    public String pollInput() {
        final String input = conn.pollInput();
        return input;
    }
}
