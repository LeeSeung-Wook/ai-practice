# 🚩 작업 보고서: 전체 페이지 계산 및 번호 UI 구현 (Step 4)

- **작업 일시**: 2026-03-23
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)

```text
1. 사용자의 요청 (브라우저: /?page=2)
   ↓
2. BoardController: 서비스의 '게시글목록보기(page)' 호출
   ↓
3. BoardService: 
   - Spring Data JPA의 Pageable 객체 생성 (page - 1, size 3, Sort ID DESC)
   - boardRepository.findAll(pageable) 호출 (데이터 조회 + COUNT 쿼리 자동 실행)
   ↓
4. BoardResponse.ListPageDTO:
   - Page<Board> 객체를 받아 총 페이지 수(totalPages), 첫/마지막 여부 계산
   - 1부터 totalPages까지 순회하며 PageNumberDTO(번호, active상태) 리스트 생성
   ↓
5. list.mustache: 
   - {{#model.pageNumbers}}로 숫자 버튼들을 출력
   - 현재 페이지에는 'active' 클래스 적용
   - 마지막 페이지일 때 '다음' 버튼에 'disabled' 클래스 적용
```

## 2. 🧩 변경된 모든 코드 포함

### 2.1 BoardResponse.java (페이징 정보 강화)
`Page` 객체의 메타데이터를 활용하여 화면에 필요한 정보들을 자동으로 추출합니다.
```java
    @Data
    public static class ListPageDTO {
        private List<ListDTO> boards; 
        private int page;             
        private int prevPage;         
        private int nextPage;         
        private int totalPages;       
        private List<PageNumberDTO> pageNumbers; // 숫자 버튼 리스트
        private boolean isFirst;      
        private boolean isLast;       

        public ListPageDTO(Page<Board> boardPage, int page) {
            this.boards = boardPage.getContent().stream().map(ListDTO::new).collect(Collectors.toList());
            this.page = page;
            this.prevPage = page - 1;
            this.nextPage = page + 1;
            this.totalPages = boardPage.getTotalPages();
            this.isFirst = boardPage.isFirst();
            this.isLast = boardPage.isLast();
            
            // 1부터 전체 페이지 수까지 번호 객체 생성
            this.pageNumbers = new ArrayList<>();
            for (int i = 1; i <= totalPages; i++) {
                pageNumbers.add(new PageNumberDTO(i, i == page));
            }
        }

        @Data
        @AllArgsConstructor
        public static class PageNumberDTO {
            private int number;
            private boolean active; // 현재 선택된 번호인지 여부
        }
    }
```

### 2.2 BoardService.java (Pageable 적용)
수동으로 계산하던 OFFSET을 `Pageable`에게 맡겨 코드가 더 표준화되었습니다.
```java
    public BoardResponse.ListPageDTO 게시글목록보기(Integer page) {
        int limit = 3; 
        // 0-index 페이지, 사이즈, 정렬 조건을 담은 Pageable 생성
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "id"));
        
        // 데이터와 페이징 메타데이터를 한 번에 조회
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return new BoardResponse.ListPageDTO(boardPage, page);
    }
```

### 2.3 list.mustache (숫자 버튼 추가)
```html
            <ul class="pagination justify-content-center">
                <li class="page-item {{#model.isFirst}}disabled{{/model.isFirst}}">
                    <a class="page-link" href="/?page={{model.prevPage}}">이전</a>
                </li>
                
                <!-- 페이지 번호들 출력 -->
                {{#model.pageNumbers}}
                <li class="page-item {{#active}}active{{/active}}">
                    <a class="page-link" href="/?page={{number}}">{{number}}</a>
                </li>
                {{/model.pageNumbers}}

                <!-- 마지막 페이지이면 '다음' 버튼 비활성화 -->
                <li class="page-item {{#model.isLast}}disabled{{/model.isLast}}">
                    <a class="page-link" href="/?page={{model.nextPage}}">다음</a>
                </li>
            </ul>
```

## 3. 🍦 상세비유 (Easy Analogy)
"이번 작업은 **내비게이션에 전체 경로와 현재 위치를 표시**하는 것과 같습니다! 
이전에는 앞뒤로만 가는 '단순 화살표'만 있었다면, 이제는 전체가 몇 km(전체 페이지)인지 알고 '현재 2km 지점(2페이지)을 지나고 있다'는 것을 지도로 보여주는 거예요. 
목적지(마지막 페이지)에 도착하면 내비게이션이 '목적지에 도착했습니다(Next 버튼 비활성화)'라고 알려주는 똑똑한 안내 시스템을 구축한 것이죠!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **Spring Data JPA `Page<T>`**:
  - **설명**: 쿼리 결과 데이터뿐만 아니라 전체 개수, 총 페이지 수, 정렬 정보 등 페이징에 필요한 모든 메타데이터를 포함하는 인터페이스입니다.
  - **이점**: `COUNT` 쿼리를 별도로 짤 필요 없이 `findAll(Pageable)` 호출 한 번으로 모든 정보를 얻을 수 있습니다.

- **Mustache `{{#active}}active{{/active}}`**:
  - **설명**: Mustache의 섹션 태그는 불리언 값이 `true`일 때만 내부 텍스트를 출력합니다.
  - **활용**: 서버에서 계산한 `active` 필드를 사용하여 현재 페이지 버튼에만 CSS 클래스를 동적으로 입힙니다.
