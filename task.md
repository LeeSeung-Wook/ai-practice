# 상세 개발 태스크 (Detailed Tasks)

## Phase 1: 기반 구조 및 핵심 도메인 설계 (Infrastructure & Core Domain)

- [x] **T-1.1 프로젝트 초기 설정**
  - [x] Spring Boot 3.x, Java 21 설정 확인
  - [x] Dependencies 설정 (Spring Data JPA, H2, Mustache, Validation, Lombok)
- [x] **T-1.2 데이터베이스 스키마 설계 및 엔티티 구현**
  - [x] `User` 엔티티 구현 (id, username, password, email, address 등)
  - [x] `Board` 엔티티 구현 (id, title, content, user, createdAt)
  - [x] `Reply` 엔티티 구현 (id, comment, user, board, createdAt)
- [x] **T-1.3 공통 응답 규격 및 유틸리티 설정**
  - [x] `Resp<T>` 클래스 설계 및 구현 (공통 응답 DTO)
  - [x] 기본 예외 처리 구조 기초 설계
- [x] **T-1.4 H2 및 JPA 환경 설정**
  - [x] `application.properties` 설정 (H2 Console, Hibernate Logging)
  - [x] 초기 데이터 구성을 위한 `data.sql` 작성
- [x] **T-1.5 공통 화면 레이아웃 구성**
  - [x] `frontend-design` 스킬을 활용하여 `header.mustache`, `footer.mustache` 파일 작성 및 공통화 적용
  - [x] Bootstrap 등을 활용한 기본 레이아웃 템플릿 구조화

## Phase 2: 회원 인증 시스템 (Membership System)

- [x] **T-2.1 회원가입 기능 및 화면 구현 (학습 및 검증 중심)**
  - [x] **[Step 1: AJAX 중복 체크]** 아이디 중복 체크 API 및 프론트엔드 비동기 통신 구현 (Fetch API 흐름 학습 및 문서화)
  - [x] **[Step 2: AOP 유효성 검사]** `UserRequest.JoinDTO` 구현 및 AOP를 활용한 공통 유효성 검사(Validation) 로직 이해 및 적용
  - [x] **[Step 3: BCrypt 암호화]** BCrypt 라이브러리 추가 및 해시 암호화 원리(Salting) 학습 후 회원정보 저장 구현
  - [x] **[Finalize]** `frontend-design` 스킬을 활용하여 `join-form.mustache` 최종 디자인 적용 및 전체 가입 프로세스 검증
- [x] **T-2.2 로그인/로그아웃 기능 및 화면 구현**
  - [x] `frontend-design` 스킬을 활용한 로그인 폼 (`login-form.mustache`) 구현
  - [x] `UserRequest.Login` 구현 (login reqDTO)
  - [x] 세션(`HttpSession`)을 이용한 인증 정보 관리
  - [x] 로그인 성공/실패 시나리오 대응 및 리다이렉션 처리
- [x] **T-2.3 회원 탈퇴 및 마이페이지 화면 구현**
  - [x] `frontend-design` 스킬을 활용한 회원 정보 수정 화면 (`update-form.mustache`) 구현 (마이페이지 겸용)
  - [x] 회원 탈퇴 API 구현
  - [x] 탈퇴 시 해당 사용자가 작성한 게시글 및 댓글 일괄 처리 (명시적 Cascade Delete 적용)
- [ ] **T-2.4 권한 관리 및 보안**
  - [ ] 인증이 필요한 페이지 접근 제어를 위한 Interceptor 구현
  - [ ] 세션 체크 로직 공통화

## Phase 3: 게시글 관리 시스템 (Core Blog Features)

- [ ] **T-3.1 게시글 CRUD 기능 및 화면 구현**
  - [ ] `frontend-design` 스킬을 활용한 게시글 목록 화면 (`list.mustache`), 상세 화면 (`detail.mustache`), 작성 화면 (`save-form.mustache`), 수정 화면 (`update-form.mustache`) 구현
  - [ ] 게시글 목록 조회 (`BoardRepository.findAll`)
  - [ ] 게시글 상세 보기 (작성자 정보 및 댓글 포함)
  - [ ] 게시글 작성/수정/삭제 API 및 서비스 로직
- [ ] **T-3.2 페이징 처리 (Pageable) 적용**
  - [ ] 목록 조회 시 `Pageable` 파라미터 적용
  - [ ] `frontend-design` 스킬을 활용하여 페이징 내비게이션 UI 구현
- [ ] **T-3.3 게시글 검색 기능 구현**
  - [ ] 제목 또는 내용 키워드 검색 기능 (`Containing` 쿼리 메서드 활용)
  - [ ] 검색 결과 페이징 연동 및 검색 폼 UI 구현
- [ ] **T-3.4 게시글 상세 정보 연동**
  - [ ] 상세 페이지에서 작성자 프로필 정보 노출

## Phase 4: 댓글 및 상호작용 (Interaction Features)

- [ ] **T-4.1 댓글 작성 기능 및 화면 연동**
  - [ ] `frontend-design` 스킬을 활용한 댓글 입력 폼 및 댓글 목록 UI (`detail.mustache` 내) 구현
  - [ ] `ReplyRequest.SaveDTO` 구현
  - [ ] AJAX 또는 Form 방식을 이용한 댓글 등록 로직
- [ ] **T-4.2 댓글 삭제 구현**
  - [ ] 댓글 삭제 API 구현 및 작성자 본인 확인 로직 적용
  - [ ] 비동기 삭제 처리 시 UI 업데이트 로직
- [ ] **T-4.3 게시글 상세 페이지 통합**
  - [ ] 게시글 조회 시 댓글 리스트 Fetch Join 또는 별도 조회 최적화
  - [ ] 댓글 개수 표시 기능 추가

## Phase 5: 예외 처리 및 안정화 (Polishing & Validation)

- [ ] **T-5.1 글로벌 예외 처리 (GlobalExceptionHandler)**
  - [ ] `@ControllerAdvice`를 이용한 공통 에러 페이지 처리
  - [ ] REST API 에러 응답(`Resp.fail`) 처리 로직 강화
- [ ] **T-5.2 데이터 유효성 검사 (Bean Validation)**
  - [ ] 모든 Request DTO에 `@NotBlank`, `@Size` 등 제약 조건 적용
  - [ ] BindingResult를 이용한 에러 메시지 사용자 노출
- [ ] **T-5.3 통합 테스트 및 최종 검수**
  - [ ] 주요 시나리오(회원가입 -> 글쓰기 -> 댓글 -> 탈퇴) 테스트
  - [ ] 성능 병목 구간(N+1 문제 등) 확인 및 해결
