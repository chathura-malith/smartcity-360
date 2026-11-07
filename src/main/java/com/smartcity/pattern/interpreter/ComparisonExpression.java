package com.smartcity.pattern.interpreter;

public class ComparisonExpression implements Expression {
    private final String variable;
    private final String operator;
    private final int value;

    public ComparisonExpression(String variable, String operator, int value) {
        this.variable = variable;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean interpret(RuleContext context) {
        int contextValue = context.getVariable(variable);

        switch (operator) {
            case ">": return contextValue > value;
            case "<": return contextValue < value;
            case "==": return contextValue == value;
            case ">=": return contextValue >= value;
            case "<=": return contextValue <= value;
            default: throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
}