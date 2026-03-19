# 🚩 작업 보고서: 마이페이지 및 회원 관리 기능 구현 (T-2.3)

- **작업 일시**: 2026-03-19
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **DTO 정의**: 
   - 조회를 위한 `UserResponse.Detail` 추가 (null 데이터 빈 문자열 처리 로직 포함).
   - 수정을 위한 `UserRequest.Update` 추가 (비밀번호 선택적 수정 가능).
2. **조회 및 수정 로직**:
   - `UserService.findById`로 마이페이지에 필요한 상세 정보 로드.
   - `UserService.update`에서 JPA **더티 체킹(Dirty Checking)**을 활용해 정보 수정 및 세션 정보 동기화.
3. **회원 탈퇴 처리**:
   - `BoardRepository`, `ReplyRepository`에 유저 ID 기반 삭제 메서드 구현.
   - `UserService.withdraw`에서 연관 데이터(댓글 -> 게시글)를 먼저 삭제하여 외래 키 제약 조건 위반 방지.
4. **UI 구현**: 
   - `update-form.mustache`를 마이페이지 허브로 구축.
   - `readonly` 속성 제거 및 `form` 태그 활성화를 통해 정보 조회와 수정을 한 화면에서 처리.

## 2. 🧩 변경된 모든 코드 포함

### UserRequest.java (Update DTO)
```java
@Data
public static class Update {
    private String password; // 입력 시에만 암호화하여 수정
    @NotBlank(message = "이메일은 필수입니다")
    private String email;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
}
```

### UserService.java (수정 및 탈퇴)
```java
@Transactional
public UserResponse.SessionUser update(Integer id, UserRequest.Update reqDTO) {
    var user = userRepository.findById(id).orElseThrow();
    // 비밀번호가 입력된 경우에만 암호화 후 업데이트
    if (reqDTO.getPassword() != null && !reqDTO.getPassword().isBlank()) {
        user.setPassword(passwordEncoder.encode(reqDTO.getPassword()));
    }
    user.setEmail(reqDTO.getEmail()); // 더티 체킹에 의해 자동 update 쿼리 발생
    return new UserResponse.SessionUser(user);
}

@Transactional
public void withdraw(Integer id) {
    replyRepository.deleteByUserId(id);      // 내가 쓴 댓글
    replyRepository.deleteByBoardUserId(id); // 내 글에 달린 모든 댓글 (FK 오류 방지)
    boardRepository.deleteByUserId(id);      // 내 글 삭제
    userRepository.deleteById(id);           // 유저 삭제
}
```

### UserController.java (세션 동기화)
```java
@PostMapping("/user/update")
public String update(UserRequest.Update reqDTO) {
    var sessionUser = (UserResponse.SessionUser) session.getAttribute("sessionUser");
    var newSessionUser = userService.update(sessionUser.getId(), reqDTO);
    session.setAttribute("sessionUser", newSessionUser); // 변경된 정보를 세션에 즉시 반영
    return "redirect:/user/update-form";
}
```

## 3. 🍦 상세비유 (Easy Analogy)
이번 작업은 **"내 집 관리 및 퇴거 신청"**과 같습니다.
- **마이페이지 조회**: 집 계약서와 내 가구 배치를 확인하는 것입니다.
- **정보 수정**: 집 인테리어를 바꾸거나 이메일 주소를 변경하는 **리모델링** 과정입니다. 바뀐 모습은 즉시 집주인(세션)에게 보고하여 최신 상태를 유지합니다.
- **회원 탈퇴**: 짐을 다 비우고(댓글/게시글 삭제) 열쇠를 반납(유저 삭제)한 뒤 집을 나가는 것입니다. 내 흔적을 먼저 지우지 않으면 문이 잠겨 나갈 수 없는(500 에러) 상황을 로직으로 해결했습니다.

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **Dirty Checking**: JPA는 트랜잭션 범위 내에서 엔티티의 변경 사항을 감지하여 트랜잭션이 끝나는 시점에 자동으로 데이터베이스에 반영합니다. 별도의 `update` 쿼리를 수동으로 작성할 필요가 없습니다.
- **Null Safety in Mustache**: Mustache는 필드가 `null`이면 키가 없는 것으로 간주해 에러를 냅니다. DTO 생성자에서 `null`을 빈 문자열(`""`)로 치환하여 렌더링 안정성을 확보했습니다.
- **FK Constraint Management**: 게시글을 삭제하기 전, 해당 게시글에 달린 모든 댓글(타인 작성 포함)을 먼저 삭제해야 데이터 무결성을 지키며 삭제를 완료할 수 있습니다.
