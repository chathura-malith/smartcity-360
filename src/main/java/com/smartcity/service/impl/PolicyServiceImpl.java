package com.smartcity.service.impl;

import com.smartcity.dto.request.PolicyEvaluateRequestDto;
import com.smartcity.dto.request.PolicyRequestDto;
import com.smartcity.dto.response.PolicyResponseDto;
import com.smartcity.model.CityPolicy;
import com.smartcity.pattern.interpreter.Expression;
import com.smartcity.pattern.interpreter.RuleContext;
import com.smartcity.pattern.interpreter.RuleParser;
import com.smartcity.service.PolicyService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PolicyServiceImpl implements PolicyService {

    @PersistenceContext(unitName = "SmartCityPU")
    private EntityManager entityManager;

    @Override
    @Transactional
    public PolicyResponseDto createPolicy(PolicyRequestDto requestDto) {
        CityPolicy policy = new CityPolicy();
        policy.setPolicyName(requestDto.getPolicyName());
        policy.setRuleExpression(requestDto.getRuleExpression());
        policy.setDescription(requestDto.getDescription());
        policy.setIsActive(true);

        entityManager.persist(policy);
        entityManager.flush();

        return mapToResponseDto(policy);
    }

    @Override
    public List<PolicyResponseDto> getAllPolicies() {
        List<CityPolicy> policies = entityManager.createQuery(
                        "SELECT p FROM CityPolicy p ORDER BY p.id DESC", CityPolicy.class)
                .getResultList();

        return policies.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean evaluatePolicy(PolicyEvaluateRequestDto evaluateDto) {
        CityPolicy policy = entityManager.find(CityPolicy.class, evaluateDto.getPolicyId());
        if (policy == null) {
            throw new IllegalArgumentException("Policy not found!");
        }

        RuleContext context = new RuleContext();
        if (evaluateDto.getEnvironmentData() != null) {
            evaluateDto.getEnvironmentData().forEach(context::setVariable);
        }

        Expression ruleExpressionTree = RuleParser.parse(policy.getRuleExpression());

        return ruleExpressionTree.interpret(context);
    }

    private PolicyResponseDto mapToResponseDto(CityPolicy policy) {
        return PolicyResponseDto.builder()
                .id(policy.getId())
                .policyName(policy.getPolicyName())
                .ruleExpression(policy.getRuleExpression())
                .description(policy.getDescription())
                .isActive(policy.getIsActive())
                .createdAt(policy.getCreatedAt())
                .build();
    }
}