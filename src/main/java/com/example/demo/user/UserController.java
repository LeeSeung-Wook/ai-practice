package com.example.demo.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo._core.handler.ex.Exception401;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    // 마이페이지 (회원정보 조회/수정 폼)
    @GetMapping("/user/update-form")
    public String updateForm(Model model) {
        var sessionUser = (UserResponse.SessionUser) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new Exception401("인증되지 않았습니다. 로그인해주세요.");
        }
        var userDetail = userService.findById(sessionUser.getId());
        model.addAttribute("userDetail", userDetail);
        return "user/update-form";
    }

    // 회원 정보 수정 처리
    @PostMapping("/user/update")
    public String update(@Valid UserRequest.Update reqDTO, BindingResult bindingResult) {
        var sessionUser = (UserResponse.SessionUser) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new Exception401("인증되지 않았습니다. 로그인해주세요.");
        }

        var newSessionUser = userService.update(sessionUser.getId(), reqDTO);
        session.setAttribute("sessionUser", newSessionUser); // 세션 동기화

        return "redirect:/user/update-form";
    }

    // 회원 탈퇴
    @PostMapping("/user/withdraw")
    public String withdraw() {
        var sessionUser = (UserResponse.SessionUser) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new Exception401("인증되지 않았습니다. 로그인해주세요.");
        }
        userService.withdraw(sessionUser.getId());
        session.invalidate(); // 탈퇴 후 세션 무효화
        return "redirect:/";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@Valid UserRequest.Join reqDTO, BindingResult bindingResult) {
        userService.join(reqDTO);
        return "redirect:/login-form";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@Valid UserRequest.Login reqDTO, BindingResult bindingResult) {
        var sessionUser = userService.login(reqDTO);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    // 회원가입 페이지 반환
    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    // 로그인 페이지 반환
    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }
}
