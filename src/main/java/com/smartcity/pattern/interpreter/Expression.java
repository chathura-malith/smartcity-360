package com.smartcity.pattern.interpreter;

public interface Expression {
    boolean interpret(RuleContext context);
}