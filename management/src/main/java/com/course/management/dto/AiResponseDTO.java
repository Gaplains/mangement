package com.course.management.dto;

import lombok.Data;
import java.util.List;

@Data
public class AiResponseDTO {
    private String type;
    private String answer;
    private List<String> suggestions;
    private List<String> relatedCourses;
}
