package com.aetheron;

public class Exit {

    public final String name;
    public final boolean isHidden;
    public final boolean isLocked;
    public final Room destination;

    public Exit(String name, Room destination) {
        this.name = name;
        this.destination = destination;
        isHidden = false;
        isLocked = false;
    }
}
