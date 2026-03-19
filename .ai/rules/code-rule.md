# Code Rules

## 1. Naming Conventions

### 1.1 Mustache Templates

- 모든 Mustache 템플릿 파일명은 **하이픈(`-`)**을 사용하여 구분한다. (Kebab-case)
  - 예: `join-form.mustache`, `login-form.mustache`, `update-form.mustache`
- 카멜 케이스(`joinForm.mustache`)는 지양한다.

## 2. Package Structure

- 도메인 중심 구조를 사용하며, 핵심 공통 기능은 `_core` 하위에 둔다.

```
com.example.demo/
  _core/utils/       (공통 유틸리티 - Resp.java 등)
  {domain}/          (도메인 별 패키지)
    {Domain}.java
    {Domain}Controller.java
    {Domain}ApiController.java
    {Domain}Service.java
    {Domain}Repository.java
    {Domain}Request.java
    {Domain}Response.java
```

## 3. Entity Rules

- PK 타입은 `Integer`를 사용한다. (GenerationType.IDENTITY)
- `@Builder`는 생성자나 메서드 위에 선언하며 클래스 레벨 사용은 지양한다.
- 모든 연관관계는 `FetchType.LAZY`를 기본으로 한다.
- 테이블명은 `{domain}_tb` 형식을 사용한다.

## 4. Service Rules

- 조회 메서드는 `@Transactional(readOnly = true)`를 사용한다.
- 쓰기 메서드(save, update, delete)는 `@Transactional`을 사용한다.
- DTO는 Service 레이어에서 변환하며, Entity를 Controller로 노출하지 않는다.

## 5. Controller Rules

- SSR(@Controller)과 REST(@RestController)를 명확히 분리한다.
- REST API 경로는 `/api`로 시작한다.
- SSR은 `HttpSession`을 사용하고 `String`을 반환한다.
- REST는 `Resp.ok()` 또는 `Resp.fail()`을 사용한다.

## 6. DTO Rules

- 파일명 형식: `{Domain}Request.java`, `{Domain}Response.java`
- 내부 클래스를 활용하여 static class로 선언한다.
- Request 클래스명은 기능명(Save, Update, Login, Join)을 사용한다.
- Response 클래스명은 데이터 범위를 기준으로 한다 (Max, Min, Detail).

## 7. JavaScript Rules

- 기본적으로 `<form>` 태그와 `name` 속성을 이용한 POST 요청을 우선한다.
- 중복 체크나 부분 갱신 등 필요한 경우에만 `fetch` (Ajax)를 사용한다.

## 8. H2 Console Configuration (Jakarta EE)

Spring Boot 3.x/4.x 이상(Jakarta EE)에서는 H2 콘솔을 정적 리소스로 오인하여 발생하는 `No static resource` 오류를 방지하기 위해 반드시 `JakartaWebServlet`을 사용하여 수동으로 등록한다.

- `ServletRegistrationBean<JakartaWebServlet>`을 사용하여 `/h2-console/*` 경로를 매핑한다.
- `setLoadOnStartup(1)`을 설정하여 서버 기동 시 즉시 로드되도록 한다.

```java
@Bean
public ServletRegistrationBean<JakartaWebServlet> h2ConsoleServlet() {
    ServletRegistrationBean<JakartaWebServlet> bean = new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");
    bean.setLoadOnStartup(1);
    return bean;
}
```
