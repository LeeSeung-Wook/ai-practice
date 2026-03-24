# 🚩 작업 보고서: 게시글 페이징 리팩토링 (Step 3)

- **작업 일시**: 2026-03-23
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)

```text
1. 사용자의 요청 (브라우저: /?page=1)
   ↓
2. BoardController: 1페이지 미만 여부만 체크 후 서비스를 호출 (게시글목록보기(page))
   ↓
3. BoardService: 
   - 1-index 페이지를 0-index OFFSET으로 변환하여 DB 조회
   - 조회된 목록과 페이징 정보(prev, next, isFirst)를 ListPageDTO에 통합
   ↓
4. BoardController: 서비스에서 받은 통합 DTO를 'model'이라는 이름으로 단 한 번 담아서 전달
   ↓
5. list.mustache: {{model.boards}}로 목록을 출력하고, {{model.prevPage}} 등으로 버튼을 구성
```

## 2. 🧩 변경된 모든 코드 포함

### 2.1 BoardResponse.java (통합 DTO)
화면 구성에 필요한 모든 데이터를 하나로 묶었습니다.
```java
    @Data
    public static class ListPageDTO {
        private List<ListDTO> boards; // 게시글 목록
        private int page;             // 현재 페이지
        private int prevPage;         // 이전 페이지
        private int nextPage;         // 다음 페이지
        private boolean isFirst;      // 첫 페이지 여부

        public ListPageDTO(List<Board> boards, int page) {
            this.boards = boards.stream().map(ListDTO::new).collect(Collectors.toList());
            this.page = page;
            this.prevPage = page - 1;
            this.nextPage = page + 1;
            this.isFirst = (page == 1);
        }
    }
```

### 2.2 BoardService.java
비즈니스 로직(페이징 계산 등)을 서비스가 책임지도록 변경했습니다.
```java
    public BoardResponse.ListPageDTO 게시글목록보기(Integer page) {
        int limit = 3; 
        int offset = (page - 1) * limit; // 1-index -> 0-index 변환
        List<Board> boards = boardRepository.mFindAll(limit, offset);
        return new BoardResponse.ListPageDTO(boards, page); // 모든 정보를 DTO에 담아 반환
    }
```

### 2.3 BoardController.java
컨트롤러는 중개 역할만 수행하며 코드가 매우 간결해졌습니다.
```java
    @GetMapping("/")
    public String home(Model model, @RequestParam(defaultValue = "1") Integer page) {
        if (page < 1) return "redirect:/?page=1";

        BoardResponse.ListPageDTO responseDTO = boardService.게시글목록보기(page);
        model.addAttribute("model", responseDTO); // 단 한 번만 addAttribute 실행
        return "board/list";
    }
```

## 3. 🍦 상세비유 (Easy Analogy)
"이번 리팩토링은 **도시락 배달 방식의 개선**과 같습니다! 
이전에는 밥 따로, 반찬 따로, 숟가락 따로 배달해서 받는 사람(컨트롤러)이 식탁에 하나씩 차려야(model.addAttribute) 했어요. 
이제는 서비스(요리사)가 모든 음식을 하나의 **'완성형 도시락 세트(통합 DTO)'**에 예쁘게 담아서 보내줍니다. 
받는 사람은 도시락 통 하나만 식탁에 올리면 되니 일이 훨씬 줄어들었고, 식탁(화면)에서도 도시락 통 안의 내용물을 꺼내 먹기만 하면 됩니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **Thin Controller & Fat Service**:
  - **설명**: 비즈니스 로직(페이지 계산, 오프셋 변환 등)을 서비스로 옮겨 컨트롤러를 가볍게 유지하는 설계 원칙입니다.
  - **이점**: 컨트롤러는 요청 전달과 응답 반환에만 집중하게 되어 가독성과 유지보수성이 높아집니다.

- **복합 DTO (Composition DTO)**:
  - **설명**: 리스트 데이터와 상태 데이터(페이징 정보)를 하나의 클래스로 구성하는 방식입니다.
  - **코드 예시**: `ListPageDTO` 클래스 내부의 `boards` 리스트와 `page` 등의 필드.
  - **이점**: 화면에서 필요로 하는 데이터의 묶음을 명확히 정의할 수 있고, 컨트롤러와 뷰 사이의 데이터 전달이 단순해집니다.
