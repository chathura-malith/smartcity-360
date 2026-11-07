package com.smartcity.pattern.interpreter;

public class RuleParser {

    public static Expression parse(String ruleExpression) {
        String[] andParts = ruleExpression.split(" AND ");
        Expression finalExpression = null;

        for (String part : andParts) {
            String[] tokens = part.trim().split(" ");

            if (tokens.length == 3) {
                String variable = tokens[0];
                String operator = tokens[1];
                int value = Integer.parseInt(tokens[2]);

                ComparisonExpression comparison = new ComparisonExpression(variable, operator, value);

                if (finalExpression == null) {
                    finalExpression = comparison;
                } else {
                    finalExpression = new AndExpression(finalExpression, comparison);
                }
            } else {
                throw new IllegalArgumentException("Invalid rule syntax: " + part);
            }
        }

        return finalExpression;
    }
}