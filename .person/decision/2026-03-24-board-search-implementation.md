# 게시글 검색 기능 구현 전략 (T-3.3)

- 날짜: 2026-03-24
- 참여: 사용자 + Gemini CLI

## 배경

게시글 목록 페이지에서 제목을 기반으로 키워드 검색 기능을 추가하고, 검색 결과 내에서도 페이징이 정상적으로 작동하도록 구현한다.

## 핵심 논의 및 결정사항

### 1. Repository 레이어
- **메서드명**: `findAllByKeyword` (사용자 제안 반영)
- **검색 대상**: 제목(`title`)으로 한정 (현재 내용 필드가 없는 점 고려)
- **구현 방식**: `@Query` 어노테이션을 사용하여 가독성 확보 및 페이징(`Pageable`) 연동
  ```java
  @Query("select b from Board b where b.title like %:keyword% order by b.id desc")
  Page<Board> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
  ```

### 2. Service 레이어 (옵션 A 채택)
- **메서드명**: `게시글목록보기(Integer page, String keyword)`
- **로직**: 검색어(`keyword`) 유무에 따른 분기 처리
  - 검색어가 없거나 빈 문자열인 경우: 기존 `findAll(pageable)` 호출
  - 검색어가 있는 경우: `findAllByKeyword(keyword, pageable)` 호출

### 3. DTO 및 페이징 상태 유지
- **BoardResponse.ListPageDTO**: `keyword` 필드를 추가하여 현재 검색 중인 키워드를 뷰로 전달
- **이유**: 페이징 버튼 클릭 시 `/?page=2&keyword=안녕`과 같이 검색 상태를 유지하기 위함

### 4. Controller 및 UI
- **Controller**: `@RequestParam(defaultValue = "") String keyword` 파라미터 추가
- **View (`list.mustache`)**: 
  - 상단에 검색 폼(Input, Button) 배치
  - 페이징 링크에 `keyword` 파라미터 동적으로 추가

## 결론

단순 통합형 방식을 채택하여 메인 페이지(`/`)에서 검색 기능을 자연스럽게 제공한다. 코드는 명확한 분기 처리를 통해 유지보수성을 높이고, DTO 확장을 통해 사용자 경험(페이징 상태 유지)을 완성한다.

## 다음 단계

1. `BoardRepository`에 `findAllByKeyword` 추가
2. `BoardResponse.ListPageDTO`에 `keyword` 필드 추가 및 생성자 수정
3. `BoardService.게시글목록보기` 로직 확장
4. `BoardController.home` 수정 및 `list.mustache` UI 업데이트
