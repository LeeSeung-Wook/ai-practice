# 🚩 작업 보고서: 로그인/로그아웃 기능 구현 (T-2.2)

- **작업 일시**: 2026-03-19
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **DTO 정의**: `UserRequest.Login` (username, password) 및 `UserResponse.SessionUser` (id, username) 추가.
2. **Repository 확장**: `UserRepository`에 유저 조회를 위한 `findByUsername` 등 쿼리 메서드 확인/추가.
3. **Service 구현**: `UserService.login`에서 `PasswordEncoder`를 사용해 비밀번호 비교 후 세션 유저 정보 반환.
4. **Controller 구현**: `UserController`에 로그인 폼(`GET`), 로그인 처리(`POST`), 로그아웃(`GET`) 엔드포인트 구현.
5. **예외 처리**: `Exception401` 추가 및 `GlobalExceptionHandler`에 등록하여 로그인 실패 시 사용자 알림 처리.
6. **UI 구현**: `login-form.mustache` 생성 및 `header.mustache` 메뉴 분기(세션 여부) 적용.

## 2. 🧩 변경된 모든 코드 포함

### UserRequest.java (Login DTO)
```java
@Data
public static class Login {
    @NotBlank(message = "유저네임은 필수입니다")
    private String username;
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}
```

### UserService.java (로그인 로직)
```java
public UserResponse.SessionUser login(UserRequest.Login reqDTO) {
    // 1. 아이디로 유저 찾기
    var userOp = userRepository.findByUsername(reqDTO.getUsername());
    if (userOp.isEmpty()) {
        throw new Exception401("유저네임을 찾을 수 없습니다.");
    }

    // 2. 비밀번호 확인 (BCrypt 매칭)
    var user = userOp.get();
    if (!passwordEncoder.matches(reqDTO.getPassword(), user.getPassword())) {
        throw new Exception401("비밀번호가 일치하지 않습니다.");
    }

    return new UserResponse.SessionUser(user);
}
```

### UserController.java (세션 관리)
```java
@PostMapping("/login")
public String login(@Valid UserRequest.Login reqDTO, BindingResult bindingResult) {
    // 1. 서비스 호출
    var sessionUser = userService.login(reqDTO);
    // 2. 세션 저장
    session.setAttribute("sessionUser", sessionUser);
    return "redirect:/";
}

@GetMapping("/logout")
public String logout() {
    session.invalidate(); // 세션 완전 무효화
    return "redirect:/";
}
```

### header.mustache (메뉴 분기)
```mustache
{{#sessionUser}}
<li class="nav-item"><a class="nav-link" href="/logout">로그아웃</a></li>
{{/sessionUser}}
{{^sessionUser}}
<li class="nav-item"><a class="nav-link" href="/login-form">로그인</a></li>
{{/sessionUser}}
```

## 3. 🍦 상세비유 (Easy Analogy)
이번 작업은 **"출입 카드 발급 시스템"**과 같습니다.
- **로그인**: 경비실(서버)에 가서 내 신분증(ID/PW)을 보여주면, 경비원(Service)이 장부(DB)를 확인하고 "본인 맞음"을 확인한 뒤 **출입 카드(SessionUser)**를 가방(Session)에 넣어주는 과정입니다.
- **세션 유지**: 이제 건물 내의 모든 방(글쓰기, 정보수정)을 들어갈 때마다 내 가방에 있는 출입 카드를 보여주기만 하면 됩니다.
- **로그아웃**: 건물을 나갈 때 출입 카드를 반납(세션 무효화)하는 것과 같습니다.

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **HttpSession**: 서버 측 메모리에 데이터를 저장하여 클라이언트의 상태를 유지하는 기술입니다. 브라우저는 `JSESSIONID`라는 쿠키를 통해 자신의 세션을 식별합니다.
- **PasswordEncoder (BCrypt)**: 비밀번호를 그대로 저장하지 않고 단방향 해시 함수로 암호화합니다. `matches()` 메서드를 통해 입력값과 암호화된 값을 안전하게 비교합니다.
- **Mustache Conditional Rendering**: `{{#sessionUser}}`는 해당 값이 존재할 때, `{{^sessionUser}}`는 존재하지 않을 때 내부 코드를 렌더링합니다. 이를 통해 동적인 UI 처리가 가능합니다.
