package com.example.demo.board;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;

    @GetMapping("/")
    public String home(Model model, 
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "") String keyword) {
        // 1페이지 미만의 페이지를 요청할 경우 1페이지로 강제 이동 (리다이렉트)
        if (page < 1) {
            return "redirect:/?page=1&keyword=" + keyword;
        }

        BoardResponse.ListPageDTO responseDTO = boardService.게시글목록보기(page, keyword);
        model.addAttribute("model", responseDTO); // 단일 속성 (규칙 A)
        return "board/list";
    }
}
