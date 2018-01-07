package com.aetheron;

public interface IProcessor {
    void process(String input, Body body, Room env, IArea area);
}