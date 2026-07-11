package com.course.management.controller;

import com.course.management.dto.AiRequestDTO;
import com.course.management.dto.AiResponseDTO;
import com.course.management.dto.Result;
import com.course.management.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiAssistantController {

    @Autowired
    private AiAssistantService aiAssistantService;

    @PostMapping("/qa")
    public Result<AiResponseDTO> answerLibraryQuestion(@RequestBody AiRequestDTO request) {
        return Result.success("图书智能问答生成成功", aiAssistantService.answerLibraryQuestion(request));
    }

    @PostMapping("/recommend")
    public Result<AiResponseDTO> recommendBooks(@RequestBody AiRequestDTO request) {
        return Result.success("智能图书推荐生成成功", aiAssistantService.recommendBooks(request));
    }

    @PostMapping("/summary")
    public Result<AiResponseDTO> generateBookSummary(@RequestBody AiRequestDTO request) {
        return Result.success("智能图书摘要生成成功", aiAssistantService.generateBookSummary(request));
    }
}
