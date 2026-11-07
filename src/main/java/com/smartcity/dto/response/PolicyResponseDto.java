package com.smartcity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyResponseDto {
    private Long id;
    private String policyName;
    private String ruleExpression;
    private String description;
    private Boolean isActive;
    private Timestamp createdAt;
}