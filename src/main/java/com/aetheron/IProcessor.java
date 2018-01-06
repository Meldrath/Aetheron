package com.aetheron;

public interface IProcessor {
    void process(String input, Body body, Environment env, Environment outerEnv);
}