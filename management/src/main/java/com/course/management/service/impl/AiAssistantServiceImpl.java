package com.course.management.service.impl;

import com.course.management.dto.AiRequestDTO;
import com.course.management.dto.AiResponseDTO;
import com.course.management.entity.Course;
import com.course.management.mapper.CourseMapper;
import com.course.management.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public AiResponseDTO answerCourseQuestion(AiRequestDTO request) {
        String question = request == null ? "" : safe(request.getQuestion());
        String normalized = question.toLowerCase(Locale.ROOT);
        AiResponseDTO response = new AiResponseDTO();
        response.setType("COURSE_QA_SIMULATED_AI");

        if (normalized.contains("选课") || normalized.contains("报名") || normalized.contains("enroll")) {
            response.setAnswer("选课流程：登录学生账号 → 进入课程中心 → 搜索课程 → 点击查看详情 → 确认可选后点击选课。系统会校验容量和重复选课。退课可在我的课程中操作。");
        } else if (normalized.contains("成绩") || normalized.contains("分数") || normalized.contains("grade")) {
            response.setAnswer("成绩由教师在成绩管理模块录入。学生可在成绩管理查看已发布成绩；如成绩为空，表示教师尚未录入或课程尚未结课。");
        } else if (normalized.contains("管理员") || normalized.contains("权限") || normalized.contains("角色")) {
            response.setAnswer("系统区分管理员、教师、学生三类角色。管理员管理用户和公告，教师发布课程、查看名单和录入成绩，学生浏览课程、选课退课和查看成绩。");
        } else {
            response.setAnswer("我可以回答选课流程、退课规则、成绩查询、课程容量、账号权限等问题。当前为本地规则 + 简单 NLP 模拟 AI，后续可替换为 OpenAI、通义千问或 Dify 知识库问答接口。");
        }

        response.setSuggestions(Arrays.asList("如何完成选课？", "教师如何录入成绩？", "管理员有哪些权限？"));
        response.setRelatedCourses(findRelatedCourseNames(question));
        return response;
    }

    @Override
    public AiResponseDTO recommendCourses(AiRequestDTO request) {
        String keyword = request == null ? "" : safe(request.getKeyword());
        List<String> related = findRelatedCourseNames(keyword);
        AiResponseDTO response = new AiResponseDTO();
        response.setType("COURSE_RECOMMENDATION_SIMULATED_AI");
        response.setRelatedCourses(related);
        if (related.isEmpty()) {
            response.setAnswer("暂未找到完全匹配的课程。建议尝试输入 Java、数据库、Web、人工智能、数学等关键词，系统会根据课程名称和简介进行匹配推荐。");
        } else {
            response.setAnswer("根据你的兴趣关键词，推荐优先了解：" + String.join("、", related) + "。推荐依据为课程名称、课程简介与输入关键词的相关度。");
        }
        response.setSuggestions(Arrays.asList("输入：想学习后端开发", "输入：人工智能入门", "输入：提高数学基础"));
        return response;
    }

    @Override
    public AiResponseDTO generateCourseSummary(AiRequestDTO request) {
        String courseName = request == null ? "" : safe(request.getCourseName());
        String description = request == null ? "" : safe(request.getDescription());
        AiResponseDTO response = new AiResponseDTO();
        response.setType("COURSE_SUMMARY_SIMULATED_AI");
        String title = StringUtils.hasText(courseName) ? courseName : "该课程";
        String body = StringUtils.hasText(description) ? description : "课程暂无详细描述";
        response.setAnswer(title + "适合希望系统学习相关知识并完成实践训练的同学。课程重点包括基础概念、核心方法、案例练习与项目应用。简介摘要：" + trim(body, 120));
        response.setSuggestions(Arrays.asList("可用于课程详情页摘要", "可扩展为调用大模型生成教学大纲", "可继续加入自动标签推荐"));
        response.setRelatedCourses(findRelatedCourseNames(courseName + " " + description));
        return response;
    }

    private List<String> findRelatedCourseNames(String keyword) {
        List<Course> courses = courseMapper.findAll();
        String key = safe(keyword).toLowerCase(Locale.ROOT);
        if (!StringUtils.hasText(key)) {
            return courses.stream().limit(5).map(Course::getCourseName).collect(Collectors.toList());
        }
        List<String> result = new ArrayList<>();
        for (Course course : courses) {
            String haystack = (safe(course.getCourseName()) + " " + safe(course.getDescription()) + " " + safe(course.getCourseCode())).toLowerCase(Locale.ROOT);
            if (haystack.contains(key) || key.contains(safe(course.getCourseName()).toLowerCase(Locale.ROOT))) {
                result.add(course.getCourseName());
            }
        }
        if (result.isEmpty()) {
            for (Course course : courses) {
                String name = safe(course.getCourseName());
                if ((key.contains("ai") || key.contains("智能")) && name.contains("智能")) result.add(name);
                if ((key.contains("web") || key.contains("前端")) && name.contains("Web")) result.add(name);
                if ((key.contains("java") || key.contains("后端")) && name.contains("Java")) result.add(name);
                if ((key.contains("数据库") || key.contains("sql")) && name.contains("数据库")) result.add(name);
                if ((key.contains("数学") || key.contains("基础")) && name.contains("数学")) result.add(name);
            }
        }
        return result.stream().distinct().limit(5).collect(Collectors.toList());
    }

    private String safe(String value) { return value == null ? "" : value.trim(); }
    private String trim(String value, int max) { return value.length() <= max ? value : value.substring(0, max) + "……"; }
}
