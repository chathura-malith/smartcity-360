package com.smartcity.service;

import com.smartcity.dto.request.PolicyEvaluateRequestDto;
import com.smartcity.dto.request.PolicyRequestDto;
import com.smartcity.dto.response.PolicyResponseDto;
import java.util.List;

public interface PolicyService {
    PolicyResponseDto createPolicy(PolicyRequestDto requestDto);
    List<PolicyResponseDto> getAllPolicies();
    boolean evaluatePolicy(PolicyEvaluateRequestDto evaluateDto);
}