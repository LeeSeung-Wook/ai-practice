# 🚩 작업 보고서: 게시글 검색 및 페이징 통합 구현

- **작업 일시**: 2026-03-24
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)

사용자가 검색어를 입력하고 페이징을 클릭할 때의 데이터 흐름입니다.

```
[사용자 화면 (list.mustache)]
      |
      | 1. 검색어(keyword) 입력 후 [검색] 클릭
      V
[Controller (BoardController.home)]
      |
      | 2. page=1, keyword="안녕" 파라미터 수신
      | 3. 서비스 호출: 게시글목록보기(1, "안녕")
      V
[Service (BoardService.게시글목록보기)]
      |
      | 4. keyword 유무 확인 (있음)
      | 5. 리포지토리 호출: findAllByKeyword("안녕", pageable)
      V
[Repository (BoardRepository.findAllByKeyword)]
      |
      | 6. DB 쿼리 실행: WHERE title LIKE '%안녕%'
      V
[DB (H2)]
      |
      | 7. 검색된 결과 반환 (Page<Board>)
      V
[다시 화면으로]
      |
      | 8. ListPageDTO에 keyword("안녕") 포함하여 전달
      | 9. 페이징 버튼 생성 시 모든 링크에 &keyword=안녕 추가
      V
[결과] 검색 상태가 유지된 페이징 화면 출력
```

## 2. 🧩 변경된 모든 코드

### 1) Repository (데이터베이스 관문)
검색 쿼리를 정의했습니다.
```java
// BoardRepository.java
@Query("select b from Board b where b.title like %:keyword% order by b.id desc")
Page<Board> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
```

### 2) DTO (데이터 전달 바구니)
검색어를 기억하고 화면에 전달할 수 있도록 필드를 추가했습니다.
```java
// BoardResponse.java
public static class ListPageDTO {
    // ... 기존 필드
    private String keyword; // 현재 검색 중인 키워드 저장

    public ListPageDTO(Page<Board> boardPage, int page, String keyword) {
        // ... 생략
        this.keyword = keyword; // 키워드 저장
    }
}
```

### 3) Service (비즈니스 로직)
검색어가 있을 때와 없을 때의 행동 지침을 정했습니다.
```java
// BoardService.java
public BoardResponse.ListPageDTO 게시글목록보기(Integer page, String keyword) {
    Pageable pageable = PageRequest.of(page - 1, 3, Sort.by(Sort.Direction.DESC, "id"));
    
    Page<Board> boardPage;
    if (keyword == null || keyword.trim().isEmpty()) {
        boardPage = boardRepository.findAll(pageable); // 검색어 없으면 전체 조회
    } else {
        boardPage = boardRepository.findAllByKeyword(keyword, pageable); // 있으면 검색 조회
    }
    
    return new BoardResponse.ListPageDTO(boardPage, page, keyword);
}
```

### 4) Controller (요청 처리기)
사용자의 검색 요청을 받아 서비스에 전달합니다.
```java
// BoardController.java
@GetMapping("/")
public String home(Model model, 
                   @RequestParam(defaultValue = "1") Integer page,
                   @RequestParam(defaultValue = "") String keyword) {
    // 검색어를 포함하여 서비스 호출
    BoardResponse.ListPageDTO responseDTO = boardService.게시글목록보기(page, keyword);
    model.addAttribute("model", responseDTO);
    return "board/list";
}
```

### 5) View (화면 레이아웃)
검색창을 만들고, 페이징 버튼이 검색어를 잃어버리지 않게 수정했습니다.
```html
<!-- list.mustache -->
<!-- 검색 폼 -->
<form action="/" method="get">
    <input type="text" name="keyword" value="{{model.keyword}}">
    <button type="submit">검색</button>
</form>

<!-- 페이징 버튼 (keyword 파라미터 추가) -->
<a href="/?page={{model.nextPage}}&keyword={{model.keyword}}">Next</a>
```

## 3. 🍦 상세비유 (Easy Analogy)

"이번 작업은 **'도서관 검색대'**와 같습니다. 

이전에는 도서관에 들어가면 그냥 1번 서가부터 순서대로 책을 보여줬다면(전체 목록 페이징), 이제는 검색창에 '해리포터'라고 치면 도서관 컴퓨터가 '해리포터'와 관련된 책들만 모아서 다시 1번 서가부터 보여주는 것과 같아요. 

중요한 건, 다음 서가(다음 페이지)로 이동할 때도 **'내가 지금 해리포터를 찾고 있다'**는 사실을 잊지 않도록 포스트잇(keyword 파라미터)을 붙여놓은 것이죠!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **Spring Data JPA @Query & LIKE**: `@Query`를 통해 직접 SQL과 유사한 JPQL을 작성했습니다. `like %:keyword%`는 검색어가 포함된 모든 제목을 찾는 '부분 일치 검색'을 수행합니다.
- **Pageable & Page<T>**: Spring의 강력한 페이징 인터페이스입니다. `findAll`뿐만 아니라 사용자 정의 쿼리인 `findAllByKeyword`에서도 `Pageable`만 넘겨주면 JPA가 알아서 `LIMIT`, `OFFSET` 쿼리를 만들어줍니다.
- **Query String 상태 유지**: HTTP는 상태가 없는(Stateless) 프로토콜입니다. 따라서 페이지를 넘길 때마다 검색어 정보를 URL의 Query String(`?keyword=...`)에 계속 실어 날라야 서버가 '아, 이 사람은 아까 그 검색어로 다음 페이지를 보고 싶어 하는구나'라고 알 수 있습니다.
