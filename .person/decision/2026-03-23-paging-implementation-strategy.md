# 페이징 구현 전략 (Step 3: Pageable 적용)

- 날짜: 2026-03-23
- 참여: 사용자 + Gemini CLI

## 배경

사용자는 화면에서 1페이지부터 보는 것이 자연스럽지만(UX), Spring Data JPA와 SQL(OFFSET)은 0페이지부터 시작하는 기술적 간극이 존재함. 이를 해결하기 위한 최적의 레이어별 역할을 정의함.

## 핵심 논의 및 결정 사항

### 1. 컨트롤러 (Controller)
- **역할**: 사용자 입력값 검증 및 0-index 변환.
- **상세**: 
  - `@RequestParam(defaultValue = "1") @Min(1) int page`를 사용하여 사람 친화적인 1페이지를 기본값으로 설정.
  - `page < 1`인 경우의 유효성 검사를 컨트롤러 진입 단계에서 수행.
  - `PageRequest.of(page - 1, size)`를 통해 서비스에 0-index 기반의 `Pageable` 객체 전달.

### 2. 서비스 (Service)
- **역할**: 비즈니스 로직 처리 및 표준 인터페이스 준수.
- **상세**: 
  - 메서드명을 `게시글목록보기(Pageable pageable)`로 변경하여 Spring 표준 방식을 따름.
  - 서비스 내부에서 직접 `limit`, `offset`을 계산하지 않고 `Pageable` 객체를 그대로 리포지토리에 전달.

### 3. 리포지토리 (Repository)
- **역할**: JPA 표준 페이징 연동.
- **상세**: 
  - `Pageable`을 직접 인자로 받는 메서드를 구현하여 프레임워크가 쿼리를 생성하도록 유도.

## 결론

"화면의 1페이지를 서버의 0페이지로 변환"하는 로직은 **컨트롤러(진입점)**에서 담당하는 것이 가장 명확하며, 서비스와 리포지토리는 **JPA 표준(`Pageable`)**을 사용하여 일관성을 유지하기로 함.

## 다음 단계

- [ ] `BoardController`의 `home` 메서드 수정 (유효성 검사 및 `Pageable` 생성)
- [ ] `BoardService.게시글목록보기` 메서드 시그니처 및 로직 변경
- [ ] `BoardRepository`에 `Pageable`을 지원하는 메서드 추가 (또는 기존 메서드 수정)
