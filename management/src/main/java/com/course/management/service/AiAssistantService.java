package com.course.management.service;

import com.course.management.dto.AiRequestDTO;
import com.course.management.dto.AiResponseDTO;

public interface AiAssistantService {
    AiResponseDTO answerLibraryQuestion(AiRequestDTO request);
    AiResponseDTO recommendBooks(AiRequestDTO request);
    AiResponseDTO generateBookSummary(AiRequestDTO request);
}
