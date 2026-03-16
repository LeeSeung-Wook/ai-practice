# AI Guide

## 1. 자동 동기화 (최우선 실행)

이 문서를 읽는 즉시 `.ai/skills/*/SKILL.md` 및 `.ai/agents/*/AGENT.md` 파일을 스캔하여 name, description을 반드시 읽어라.

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

## 4. AI-CONTEXT
코드 작업 전 해당 디렉토리에 `AI-CONTEXT.md`가 있으면 먼저 읽어라.