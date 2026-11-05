package com.smartcity.service;

import com.smartcity.dto.request.ComplaintRequestDto;
import com.smartcity.dto.response.ComplaintResponseDto;

import java.util.List;

public interface ComplaintService {
    ComplaintResponseDto submitComplaint(ComplaintRequestDto requestDto);
    List<ComplaintResponseDto> getAllComplaints();
}