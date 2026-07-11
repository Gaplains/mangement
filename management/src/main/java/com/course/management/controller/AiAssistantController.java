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
    public Result<AiResponseDTO> answerCourseQuestion(@RequestBody AiRequestDTO request) {
        return Result.success("AI问答生成成功", aiAssistantService.answerCourseQuestion(request));
    }

    @PostMapping("/recommend")
    public Result<AiResponseDTO> recommendCourses(@RequestBody AiRequestDTO request) {
        return Result.success("AI推荐生成成功", aiAssistantService.recommendCourses(request));
    }

    @PostMapping("/summary")
    public Result<AiResponseDTO> generateCourseSummary(@RequestBody AiRequestDTO request) {
        return Result.success("AI摘要生成成功", aiAssistantService.generateCourseSummary(request));
    }
}
