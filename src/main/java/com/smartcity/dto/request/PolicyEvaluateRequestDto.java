package com.smartcity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEvaluateRequestDto {
    private Long policyId;
    private Map<String, Integer> environmentData;
}