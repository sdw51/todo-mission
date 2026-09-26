# Todo Mission

할 일 생성·조회·수정·완료 상태 전환·삭제를 제공하는 Spring Boot REST API입니다.

## 1. 실행 방법

- **JDK 21**이 필요합니다. `JAVA_HOME`과 IntelliJ의 Project SDK·Gradle JVM을 JDK 21로 설정합니다.
- **Spring Boot 3.5.16**, **Gradle 8.14.3**을 사용합니다.
- **H2 인메모리 DB**를 사용하므로 별도 DB 설치는 필요 없습니다. 앱 종료 시 데이터가 사라집니다.
- 프로젝트 루트에서 실행합니다. 최초 실행에는 Gradle과 의존성 다운로드를 위한 인터넷 연결이 필요합니다.

```powershell
# Windows PowerShell
.\gradlew.bat bootRun
```

```bash
# macOS / Linux
sh gradlew bootRun
```

서버: `http://localhost:8080`<br>
H2 콘솔: `http://localhost:8080/h2-console` (`jdbc:h2:mem:todo`, 사용자 `sa`, 비밀번호 없음)

## 2. API 명세

요청·응답은 JSON입니다. 본문이 있는 요청에는 `Content-Type: application/json`을 지정합니다.

| 기능 | 메서드 | 주소 | 요청 본문 | 성공 응답 본문 | 상태 코드 |
| --- | --- | --- | --- | --- | --- |
| 생성 | POST | `/v1/todos` | 제목·설명 | Todo 객체 | 201 |
| 목록 | GET | `/v1/todos` | 없음 | Todo 배열, 없으면 `[]` | 200 |
| 단일 조회 | GET | `/v1/todos/{id}` | 없음 | Todo 객체 | 200 |
| 수정 | PUT | `/v1/todos/{id}` | 제목·설명 | Todo 객체 | 200 |
| 완료 상태 전환 | PATCH | `/v1/todos/{id}/completed` | 없음 | Todo 객체 | 200 |
| 삭제 | DELETE | `/v1/todos/{id}` | 없음 | 없음 | 204 |

생성·수정 요청 본문:

```json
{"title":"test1","description":"Todo API test"}
```

`title`은 필수이며 공백만 입력할 수 없고 최대 200자입니다. `description`은 선택이며 최대 1,000자입니다. 수정 시 설명을 생략하면 null로 변경됩니다. 완료 상태는 전환 API를 호출할 때마다 반전됩니다.

Todo 응답 본문:

```json
{"id":1,"title":"test1","description":"Todo API test","completed":false,"createdAt":"2026-09-27T04:44:38.842359","updatedAt":"2026-09-27T04:44:38.842359"}
```

오류 응답은 `{"status":400,"message":"제목을 입력해주세요"}` 형태입니다. 생성·수정의 검증 실패는 HTTP 400입니다. ID를 사용하는 API에서 Todo가 없으면 본문은 `status: 404`이지만, **현재 예외 처리기의 `badRequest()` 때문에 실제 HTTP 상태는 400**입니다. 등록되지 않은 주소는 Spring Boot 기본 오류 응답으로 HTTP 404를 반환합니다.

## 3. 설계 설명

- `/v1/todos`는 API 버전과 할 일 컬렉션을, `/{id}`는 개별 할 일을 나타냅니다. 동작은 HTTP 메서드로 구분합니다.
- PUT은 제목·설명을 교체하고, PATCH는 완료 상태만 변경하는 데 사용합니다.
- 생성은 새 리소스가 생기므로 201, 조회·수정은 결과를 반환하므로 200, 삭제는 응답 본문이 필요 없어 204를 사용합니다.
- 잘못된 입력은 400, 없는 리소스는 404로 구분하는 설계입니다. Todo 미존재 처리의 HTTP 상태는 위에 적은 대로 수정이 필요합니다.
- H2는 별도 서버 없이 실행할 수 있어 과제 실행과 API 확인이 간편합니다. 인메모리 방식이라 영구 저장에는 적합하지 않습니다.

## 4. 실행 결과

로컬 서버에 HTTP 클라이언트로 순서대로 호출한 실제 결과입니다. 아래 요청은 IntelliJ HTTP Client 형식이며, 응답 헤더는 상태 코드만 표시했습니다.

### ① 만들기 → 201

```http
POST http://localhost:8080/v1/todos
Content-Type: application/json

{"title":"test1","description":"Todo API test"}
```

```http
HTTP/1.1 201 Created

{"completed":false,"createdAt":"2026-09-27T04:44:38.842359","description":"Todo API test","id":1,"title":"test1","updatedAt":"2026-09-27T04:44:38.842359"}
```

### ② 목록 → 200

```http
GET http://localhost:8080/v1/todos
```

```http
HTTP/1.1 200 OK

[{"completed":false,"createdAt":"2026-09-27T04:44:38.842359","description":"Todo API test","id":1,"title":"test1","updatedAt":"2026-09-27T04:44:38.842359"}]
```

### ③ 완료 처리 → 200

```http
PATCH http://localhost:8080/v1/todos/1/completed
```

```http
HTTP/1.1 200 OK

{"completed":true,"createdAt":"2026-09-27T04:44:38.842359","description":"Todo API test","id":1,"title":"test1","updatedAt":"2026-09-27T04:44:38.842359"}
```

### ④ 삭제 → 204

```http
DELETE http://localhost:8080/v1/todos/1
```

```http
HTTP/1.1 204 No Content
```

응답 본문은 없습니다.

### ⑤ 빈 제목 → 400

```http
POST http://localhost:8080/v1/todos
Content-Type: application/json

{"title":"","description":"invalid"}
```

```http
HTTP/1.1 400 Bad Request

{"status":400,"message":"제목을 입력해주세요"}
```

### ⑥ 존재하지 않는 주소 → 404

```http
GET http://localhost:8080/v1/unknown
```

```http
HTTP/1.1 404 Not Found

{"timestamp":"2026-09-26T19:44:39.061Z","status":404,"error":"Not Found","message":"No static resource v1/unknown.","path":"/v1/unknown"}
```

위 응답은 긴 `trace` 필드만 생략했습니다.

참고로 삭제한 Todo를 `GET /v1/todos/1`로 조회한 실제 결과는 아래와 같습니다. Todo 미존재의 HTTP 404 사례는 아직 충족하지 않습니다.

```http
HTTP/1.1 400 Bad Request

{"status":404,"message":"Todo를 찾을 수 없습니다."}
```
