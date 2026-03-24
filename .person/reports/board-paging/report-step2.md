# 🚩 작업 보고서: 게시글 페이징 [Step 2] SQL 기초 페이징 (LIMIT/OFFSET)

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
```text
[Repository] JPQL 명시적 쿼리 추가 (@Query)
             (SELECT ... LIMIT :limit OFFSET :offset)
        ↓
[Service] 오프셋 계산 로직 구현 (page * 3)
        ↓
[Controller] @RequestParam(defaultValue = "0") page 추가
        ↓
[Test] URL 쿼리 파라미터(?page=n)를 통한 페이징 동작 확인
```

1. **명시적 쿼리 작성**: JPA의 기본 기능을 넘어, JPQL에서 직접 `LIMIT`와 `OFFSET`을 사용하는 커스텀 쿼리를 리포지토리에 추가했습니다.
2. **동적 오프셋 계산**: 사용자가 요청한 페이지 번호에 따라 DB에서 어디서부터 데이터를 가져올지 결정하는 로직을 서비스에 구현했습니다.
3. **파라미터 바인딩**: 컨트롤러에서 클라이언트의 요청(`?page=1`)을 받아 서비스 계층으로 자연스럽게 흐르도록 설계했습니다.

## 2. 🧩 변경된 모든 코드 포함

### BoardRepository.java (명시적 JPQL 쿼리)
```java
@Query("SELECT b FROM Board b ORDER BY b.id DESC LIMIT :limit OFFSET :offset")
List<Board> mFindAll(@Param("limit") Integer limit, @Param("offset") Integer offset);
```
- **설명**: Hibernate 6+ 부터 지원되는 JPQL `LIMIT/OFFSET` 문법을 사용하여 DB 레벨에서 결과셋을 제한합니다.

### BoardService.java (페이징 로직)
```java
public List<BoardResponse.ListDTO> 게시글목록보기(Integer page) {
    int limit = 3; // 한 페이지에 3개씩
    int offset = page * limit; // 시작 위치 계산 (0페이지 -> 0, 1페이지 -> 3)
    
    // 명시적인 LIMIT/OFFSET 쿼리 호출
    List<Board> boards = boardRepository.mFindAll(limit, offset);
    return boards.stream().map(BoardResponse.ListDTO::new).collect(Collectors.toList());
}
```

### BoardController.java (요청 파라미터 수신)
```java
@GetMapping("/")
public String home(Model model, @RequestParam(defaultValue = "0") Integer page) {
    // page 파라미터를 받아 서비스로 전달
    List<BoardResponse.ListDTO> boards = boardService.게시글목록보기(page);
    model.addAttribute("boards", boards);
    return "board/list";
}
```

## 3. 🍦 상세비유 (Easy Analogy)
"이번 작업은 **'책의 특정 페이지 펼치기'**와 같습니다. 이전 [Step 1]에서는 책 전체를 한 번에 보여주려고 했다면, 이제는 한 번에 3줄(Limit)씩만 보여주기로 했습니다. '1페이지를 보여줘'라고 하면, 0번째 줄부터 3줄을 읽고, '2페이지를 보여줘'라고 하면 앞에 있는 3줄을 건너뛰고(Offset) 그 다음부터 3줄을 읽어서 보여주는 원리입니다. 독자(사용자)는 이제 한 번에 너무 많은 정보를 보지 않아도 되어 가독성이 좋아졌습니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **LIMIT & OFFSET**: 
  - **LIMIT**: 결과 집합에서 가져올 최대 행 수를 지정합니다.
  - **OFFSET**: 결과 집합의 시작 지점에서 건너뛸 행 수를 지정합니다.
  - **핵심 공식**: `OFFSET = (요청 페이지 번호) * (페이지당 행 수)`

- **JPQL LIMIT/OFFSET (Hibernate 6+)**: 
  - 과거에는 JPQL에서 `LIMIT`를 직접 쓸 수 없어 `Native Query`를 쓰거나 `setFirstResult/setMaxResults`를 사용해야 했으나, 최신 Hibernate 버전(Spring Boot 3+ 이상)부터는 JPQL에서도 표준 SQL처럼 `LIMIT`와 `OFFSET`을 지원하여 더욱 명시적인 코드 작성이 가능해졌습니다.

- **Default Value 처리**: 
  - `@RequestParam(defaultValue = "0")`을 통해 사용자가 페이지 번호를 전달하지 않았을 때(최초 접속 등) 발생할 수 있는 에러를 방지하고 기본적으로 첫 페이지를 보여주도록 안전하게 설계했습니다.
