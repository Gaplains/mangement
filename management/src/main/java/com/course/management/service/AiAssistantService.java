package com.course.management.service;

import com.course.management.dto.AiRequestDTO;
import com.course.management.dto.AiResponseDTO;

public interface AiAssistantService {
    AiResponseDTO answerCourseQuestion(AiRequestDTO request);
    AiResponseDTO recommendCourses(AiRequestDTO request);
    AiResponseDTO generateCourseSummary(AiRequestDTO request);
}
