# 🚩 작업 보고서: 게시글 페이징 [Step 1] 데이터 준비 및 기본 목록 보기

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
```text
[DB] 20개 이상의 더미 데이터 추가 (data.sql)
        ↓
[DTO] 화면 전달용 BoardResponse.ListDTO 정의
        ↓
[Service] 게시글목록보기() 메서드 구현 (Entity -> DTO 변환)
        ↓
[Controller] 홈(/) 경로 매핑 수정 및 데이터 바인딩
        ↓
[View] list.mustache 생성 및 목록 출력 UI 구현
```

1. **데이터 확보**: 페이징 테스트를 위해 20개 이상의 데이터를 `data.sql`에 추가했습니다.
2. **데이터 가공**: 엔티티를 직접 노출하지 않고 필요한 정보만 담는 `ListDTO`를 설계했습니다.
3. **로직 연결**: 서비스 계층에서 데이터를 가져와 DTO로 변환하고, 컨트롤러에서 이를 받아 화면으로 전달했습니다.
4. **시각화**: Mustache 템플릿의 반복문(`{{#boards}}`)을 사용하여 깔끔한 테이블 형태로 목록을 구현했습니다.

## 2. 🧩 변경된 모든 코드 포함

### BoardResponse.java (DTO 추가)
```java
@Data
public static class ListDTO {
    private Integer id;
    private String title;
    private String username;

    // 엔티티를 DTO로 변환하는 생성자
    public ListDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUser().getUsername();
    }
}
```

### BoardService.java (서비스 메서드)
```java
@Transactional(readOnly = true)
public List<BoardResponse.ListDTO> 게시글목록보기() {
    // 1. 모든 게시글 조회
    List<Board> boards = boardRepository.findAll();
    // 2. Stream을 사용하여 Entity 리스트를 DTO 리스트로 변환
    return boards.stream().map(BoardResponse.ListDTO::new).collect(Collectors.toList());
}
```

### BoardController.java (컨트롤러 매핑)
```java
@GetMapping("/")
public String home(Model model) {
    // 서비스 호출 후 결과를 모델에 담아 뷰로 전달
    List<BoardResponse.ListDTO> boards = boardService.게시글목록보기();
    model.addAttribute("boards", boards);
    return "board/list";
}
```

### list.mustache (목록 화면)
```html
<tbody>
    {{#boards}} <!-- 리스트 순회 시작 -->
    <tr>
        <td>{{id}}</td>
        <td>{{title}}</td>
        <td>{{username}}</td>
        <td><a href="/board/{{id}}" class="btn btn-primary btn-sm">상세보기</a></td>
    </tr>
    {{/boards}} <!-- 리스트 순회 끝 -->
</tbody>
```

## 3. 🍦 상세비유 (Easy Analogy)
"이번 작업은 **'뷔페 식탁 세팅'**과 같습니다. 먼저 손님들이 마음껏 드실 수 있도록 맛있는 음식(더미 데이터)을 주방에 가득 채워두고, 예쁜 접시(DTO)를 준비했습니다. 주방장(Service)이 솥에 있는 음식(Entity)을 접시에 보기 좋게 담아내면, 홀 서빙(Controller)이 그 접시를 들고 손님들이 기다리는 테이블(Mustache 화면)에 가져다 놓는 과정입니다. 이제 손님들은 테이블에 차려진 음식을 편하게 구경(목록 보기)할 수 있게 되었습니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **DTO (Data Transfer Object)**: 
  - **설명**: 데이터베이스 엔티티 구조를 화면에 그대로 노출하지 않고, 보안과 효율성을 위해 화면(View)에서 딱 필요한 데이터만 골라 담는 바구니 역할을 합니다.
  - **이유**: `Board` 엔티티에는 작성자의 비밀번호가 포함된 `User` 객체나 복잡한 연관 관계가 섞여 있을 수 있는데, DTO를 쓰면 `username` 같은 필요한 문자열만 깔끔하게 뽑아서 보낼 수 있습니다.

- **Mustache Iterator (`{{#items}}...{{/items}}`)**: 
  - **설명**: 서버에서 넘겨준 리스트(List) 데이터를 화면에서 하나씩 꺼내어 반복해서 그려주는 강력한 문법입니다.
  - **장점**: 데이터가 1개든 100개든 동일한 HTML 구조를 반복해서 생성해주므로 목록형 UI를 만들 때 필수적입니다.

- **Stream API (`map`, `collect`)**:
  - **설명**: 자바 8부터 도입된 기능으로, 리스트 내의 각 요소를 다른 형태로 변환(map)하거나 다시 리스트로 묶는(collect) 작업을 선언적으로 처리할 수 있게 해줍니다.
  - **코드 예시**: `boards.stream().map(BoardResponse.ListDTO::new).collect(Collectors.toList())`
