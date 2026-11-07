package com.smartcity.pattern.interpreter;

import java.util.HashMap;
import java.util.Map;

public class RuleContext {
    private final Map<String, Integer> variables = new HashMap<>();

    public void setVariable(String name, int value) {
        variables.put(name.toUpperCase(), value);
    }

    public int getVariable(String name) {
        return variables.getOrDefault(name.toUpperCase(), 0);
    }
}