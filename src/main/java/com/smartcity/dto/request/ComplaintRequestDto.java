package com.smartcity.dto.request;

import com.smartcity.model.enums.ComplaintSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintRequestDto {
    private String description;
    private ComplaintSeverity severity;
}