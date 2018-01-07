package com.aetheron;

import java.util.Collection;

interface Environment {
    Collection<Body> getLocalBodies();
    String getDescription();
}
