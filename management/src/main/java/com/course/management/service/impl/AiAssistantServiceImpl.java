package com.course.management.service.impl;

import com.course.management.dto.AiRequestDTO;
import com.course.management.dto.AiResponseDTO;
import com.course.management.entity.Book;
import com.course.management.mapper.BookMapper;
import com.course.management.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {
    @Autowired private BookMapper bookMapper;

    @Override public AiResponseDTO answerLibraryQuestion(AiRequestDTO request) {
        String q = request == null ? "" : safe(request.getQuestion());
        String n = q.toLowerCase(Locale.ROOT);
        AiResponseDTO r = new AiResponseDTO();
        r.setType("LIBRARY_QA_SIMULATED_AI");
        if (n.contains("借") || n.contains("borrow")) r.setAnswer("借阅流程：登录读者账号 → 检索图书 → 提交借阅申请 → 管理员审批 → 审批通过后形成借阅中记录，借期默认 30 天。");
        else if (n.contains("还") || n.contains("return")) r.setAnswer("归还流程：在我的借阅中找到借阅中记录，联系管理员办理归还；管理员点击归还后系统自动恢复该书可借库存。");
        else if (n.contains("逾期") || n.contains("到期")) r.setAnswer("系统保留到期日期，管理员可按到期日期筛选记录。演示版默认借期 30 天，可扩展为逾期自动提醒和罚金规则。");
        else if (n.contains("管理员") || n.contains("权限")) r.setAnswer("管理员可维护图书、分类、读者和借阅审批；普通用户可搜索图书、提交借阅申请、查看个人借阅记录和使用智能推荐。");
        else r.setAnswer("我是图书馆智能助手，可以回答借阅规则、归还流程、库存状态、分类检索和图书推荐问题。本模块为本地规则 + 简单 NLP 模拟 AI，可替换为大模型 API。 ");
        r.setSuggestions(Arrays.asList("如何借阅一本书？","有没有人工智能入门书籍？","归还图书后库存如何变化？"));
        r.setRelatedCourses(findRelatedBookNames(q));
        return r;
    }

    @Override public AiResponseDTO recommendBooks(AiRequestDTO request) {
        String keyword = request == null ? "" : safe(request.getKeyword());
        List<String> related = findRelatedBookNames(keyword);
        AiResponseDTO r = new AiResponseDTO();
        r.setType("BOOK_RECOMMENDATION_SIMULATED_AI");
        r.setRelatedCourses(related);
        r.setAnswer(related.isEmpty() ? "暂未找到完全匹配的图书。可尝试输入：人工智能、Java、Web、数据库、文学、历史等兴趣关键词。" : "根据你的兴趣关键词，推荐优先阅读：" + String.join("、", related) + "。推荐依据为书名、作者、分类和简介关键词匹配。");
        r.setSuggestions(Arrays.asList("输入：人工智能入门","输入：适合 Web 开发初学者","输入：经典文学作品"));
        return r;
    }

    @Override public AiResponseDTO generateBookSummary(AiRequestDTO request) {
        String name = request == null ? "" : safe(request.getCourseName());
        String desc = request == null ? "" : safe(request.getDescription());
        AiResponseDTO r = new AiResponseDTO();
        r.setType("BOOK_SUMMARY_SIMULATED_AI");
        String title = StringUtils.hasText(name) ? name : "该图书";
        r.setAnswer(title + "适合希望系统了解相关主题的读者。内容摘要：" + trim(StringUtils.hasText(desc) ? desc : "暂无详细简介，可由管理员补充作者、主题、适读人群和馆藏位置。", 140));
        r.setSuggestions(Arrays.asList("可用于图书详情页简介优化","可扩展为调用大模型生成摘要","可继续加入自动标签推荐"));
        r.setRelatedCourses(findRelatedBookNames(name + " " + desc));
        return r;
    }

    private List<String> findRelatedBookNames(String keyword) {
        List<Book> books = bookMapper.search(null, null, "ON_SHELF");
        String key = safe(keyword).toLowerCase(Locale.ROOT);
        if (!StringUtils.hasText(key)) return books.stream().limit(5).map(Book::getTitle).collect(Collectors.toList());
        List<String> result = new ArrayList<>();
        for (Book b : books) {
            String h = (safe(b.getTitle()) + " " + safe(b.getAuthor()) + " " + safe(b.getDescription()) + " " + safe(b.getCategoryName())).toLowerCase(Locale.ROOT);
            if (h.contains(key) || key.contains(safe(b.getTitle()).toLowerCase(Locale.ROOT))) result.add(b.getTitle());
        }
        if (result.isEmpty()) for (Book b : books) {
            String h = safe(b.getTitle()) + safe(b.getDescription()) + safe(b.getCategoryName());
            if ((key.contains("ai") || key.contains("智能")) && h.contains("智能")) result.add(b.getTitle());
            if ((key.contains("web") || key.contains("java")) && (h.contains("Web") || h.contains("Java"))) result.add(b.getTitle());
            if (key.contains("数据库") && h.contains("数据库")) result.add(b.getTitle());
            if ((key.contains("文学") || key.contains("小说")) && h.contains("文学")) result.add(b.getTitle());
        }
        return result.stream().distinct().limit(5).collect(Collectors.toList());
    }
    private String safe(String v){ return v==null?"":v.trim(); }
    private String trim(String v,int max){ return v.length()<=max?v:v.substring(0,max)+"……"; }
}
