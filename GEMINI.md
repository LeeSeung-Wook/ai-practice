## 0. 중요규칙

버전 : spring 4.0.3
사용기술 : mustache, h2, jpa
http메서드 : get, post만 사용하여 구현한다. *ApiController는 ajax가 필요할때만 사용한다.
http요청방법 : form태그 사용. x-www-form-urlencoded
*Service.java의 메서드명은 반드시 한글로 작성한다.
model에 데이터를 담을때 규칙(A, B 참고)
A : model.addAttribute("model", 오브젝트);
B : model.addAttribute("models", 컬렉션);
TODO.md 파일에 작업이 하나라도 완료되면 x표시한다. (예시: - [x] T-1.1 )

## 1. 자동 동기화 (최우선 실행)

이 문서를 읽는 즉시 `.ai/skills/*/SKILL.md` 및 `.ai/agents/*/AGENT.md` 파일을 스캔하여 description을 반드시 읽어라.

---

## 2. 참조 규칙 (Memory Load)

아래 파일을 읽고 규칙을 반드시 준수한다.

- **컨벤션**: `.ai/rules/*.md`

---

## 3. 활동 로깅 (Activity Logging)

`.ai/hooks/log-activity.sh`를 통해 `.person/logs/activity.log`에 기록된다.
커스텀 스킬의 SKILL.md를 읽어 실행할 때, **반드시** 아래 명령을 함께 실행하여 로그를 남겨라:

```bash
echo '{"tool_name":"Skill","tool_input":{"skill":"<스킬명>"}}' | bash .ai/hooks/log-activity.sh
```

---

## 4. 사용언어

모든 작업은 한국어로 진행한다.
