# 기술 명세서 (Technical Specification)

## 1. 기술 스택 (Technology Stack)

### 1.1 Backend

- **Language**: Java 21
- **Framework**: Spring Boot 4.0.3
- **Build Tool**: Gradle
- **Security**: HttpSession 기반 인증 , security 사용안함. 인터셉터 인증
- **View Engine**: Mustache (Server-Side Rendering)

### 1.2 Data & Persistence

- **Database**: H2 (In-Memory) - 개발 및 테스트용
- **ORM**: Spring Data JPA (Hibernate)
- **Database Connection Pool**: HikariCP
- **Key Strategy**: `GenerationType.IDENTITY` (MySQL/H2 호환)

### 1.3 Infrastructure & Tools

- **Server**: Embedded Tomcat 10
- **Logging**: SLF4J (Logback)
- **Utility**: Lombok

## 2. 프로젝트 구조 (Project Architecture)

### 2.1 패키지 구조 (Layered Architecture)

본 프로젝트는 도메인 기반 패키지 구조를 따르며, 각 도메인 내에서 레이어를 분리합니다.

- `com.example.demo`
  - `user`: 사용자 관련 (Entity, Repository, Service, Controller, DTO)
  - `board`: 게시글 관련 (Entity, Repository, Service, Controller, DTO)
  - `reply`: 댓글 관련 (Entity, Repository, Service, Controller, DTO)
  - `_core`: 공통 유틸리티, 예외 처리, 응답 포맷 (`Resp.java`)

### 2.2 공통 규칙 (Common Rules)

- **OSIV (Open Session In View)**: `false` (성능 및 커넥션 효율을 위해 비활성화)
- **Lazy Loading**: 모든 연관관계는 `FetchType.LAZY`를 기본으로 설정.
- **Transaction**: 서비스 레이어에서 `@Transactional`을 사용하여 원자성 보장.
- **DTO**: API 통신 및 데이터 이동 시 평탄화된 DTO를 사용하며, Entity 노출을 최소화함.

## 3. 주요 기능 구현 기술 (Core Implementation)

### 3.1 회원 관리 (User Management)

- **인증**: `HttpSession`을 통한 세션 관리 방식을 기본으로 하며, 추후 JWT 전환 가능성을 고려한 설계.
- **보안**: 비밀번호 암호화 저장 (추후 적용 예정).

### 3.2 게시글 & 댓글 (Post & Comment)

- **페이징**: `Spring Data JPA`의 `Pageable`, `Page` 인터페이스를 활용한 목록 조회.
- **연관관계**: `Board` (게시글) - `User` (작성자), `Reply` (댓글) - `Board`, `User` 간의 다대일(`@ManyToOne`) 관계 설정.

## 4. 데이터베이스 스키마 (Database Schema Summary)

- `user_tb`: 사용자 정보 (아이디, 비밀번호, 이메일, 주소 등)
- `board_tb`: 게시글 정보 (제목, 내용, 작성자 ID, 생성시간)
- `reply_tb`: 댓글 정보 (내용, 작성자 ID, 게시글 ID, 생성시간)

## 5. API 응답 규격 (Response Format)

모든 API 응답은 `_core/utils/Resp.java`를 통해 표준화된 형식을 따릅니다.

```json
{
  "status": 200,
  "msg": "성공",
  "body": { ... }
}
```
