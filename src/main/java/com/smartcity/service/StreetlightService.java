package com.smartcity.service;

import com.smartcity.dto.request.StreetlightRequestDto;
import com.smartcity.dto.response.StreetlightResponseDto;
import java.util.List;

public interface StreetlightService {
    StreetlightResponseDto addStreetlight(StreetlightRequestDto requestDto);
    List<StreetlightResponseDto> getAllStreetlights();
}