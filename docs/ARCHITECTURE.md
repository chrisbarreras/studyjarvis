# StudyJarvis — Architecture

This document is an orientation for anyone reading the StudyJarvis codebase for the first time. It explains the two run modes, the pipeline they share, the package layout, and where the external integrations live. Each section links to a diagram with the details.

## Purpose

StudyJarvis helps a student turn a pile of lecture PDFs and PowerPoints into derived study material — notes, study guides, key-point lists, quizzes, and an interactive Q&A loop — by delegating the hard part to Gemini (via Vertex AI) and using Google Cloud Storage as the staging area for multi-modal context.

## Two run modes, one pipeline

There are two entry points that deliver the same features through different interfaces:

- **CLI** — [Main.java](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/Main.java) boots [CommandSession](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/command/CommandSession.java), a `while`-loop prompt that dispatches typed commands through [CommandParser](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/command/CommandParser.java) to concrete [Command](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/command/Command.java) subclasses.
- **HTTP server** — [StudyJarvisServer.java](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/StudyJarvisServer.java) is a Javalin app that exposes the same features as authenticated POST endpoints under `/secure/jarvis/*`, plus admin CRUD for users and sessions, plus a file upload/prepare pipeline.

Both modes converge on [Jarvis.java](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/Jarvis.java), which holds a [Gemini](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/Gemini.java) client and a [GoogleBucket](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/GoogleBucket.java) handle, and exposes the "create X" methods the UIs call.

The shared pipeline for every Jarvis feature is:

1. **Upload** raw files to a local staging directory.
2. **Extract** text/images via [PDFExtractor](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/extraction/PDFExtractor.java) or [PowerPointExtractor](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/extraction/PowerPointExtractor.java).
3. **Push** the extracted artifacts into a per-user prefix of a Google Cloud Storage bucket.
4. **Prompt** Gemini with the GCS URIs as multi-modal context plus a feature-specific instruction.
5. **Return** the model's text response to the caller.

See [context](diagrams/context.md), [containers](diagrams/containers.md), and [sequence — ask a question](diagrams/sequence-ask-question.md) for visuals of this pipeline.

## Package layout

| Package | Role |
| --- | --- |
| `com.christophertbarrerasconsulting.studyjarvis` | Core glue: [Jarvis](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/Jarvis.java), [Gemini](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/Gemini.java), [GoogleBucket](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/GoogleBucket.java), `Main` |
| `...command` | CLI command pattern — [Command](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/command/Command.java), [CommandParser](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/command/CommandParser.java), [CommandSession](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/command/CommandSession.java), and all concrete `*Command` classes |
| `...server` | Javalin entry point, handlers, auth, and DAO-style readers/writers backed by PostgreSQL |
| `...extraction` | PDF and PowerPoint extraction |
| `...file` | Local filesystem helpers, config loading ([AppSettings](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/file/AppSettings.java), [AppConfigPath](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/file/AppConfigPath.java), [ConfigReader](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/file/ConfigReader.java)) |
| `...quiz` | Interactive quiz state machine and console runner |
| `...user` | Plain data types ([User](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/user/User.java), [Session](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/user/Session.java), request/response DTOs) |

## External integrations

- **Vertex AI** — [Gemini](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/Gemini.java) wraps `VertexAI` + `GenerativeModel`. It accumulates an `ArrayList<Object> parts` of prior prompts/responses as conversational context and retries up to five times on `ResourceExhaustedException`.
- **Google Cloud Storage** — [GoogleBucket](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/GoogleBucket.java) uploads/clears/lists objects under a `"user <userId>:"` prefix. The prefix is how the server partitions storage per authenticated user.
- **PostgreSQL** — [Database.java](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/Database.java) opens connections from `STUDYJARVIS_DB_URL` / `USER` / `PASSWORD` env vars. Readers/writers are static facades: [UserReader](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/UserReader.java), [UserWriter](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/UserWriter.java), [SessionReader](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/SessionReader.java), [SessionWriter](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/SessionWriter.java).

## Configuration

- **App settings** (both modes) — a Java `Properties` file at `$APPDATA\studyjarvis.properties` (Windows) / `~/Library/Application Support/studyjarvis.properties` (macOS) / `~/.config/studyjarvis.properties` (Linux). Keys: `BucketName`, `ExtractFolder`, `GeminiProjectId`, `GeminiModelName`, `GeminiLocation`. The CLI loads them at startup via [LoadLocalSettingsCommand](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/command/LoadLocalSettingsCommand.java); the server reads them on demand via `AppSettings.*`.
- **Server secrets** (server only) — `STUDYJARVIS_DB_URL`, `STUDYJARVIS_DB_USER`, `STUDYJARVIS_DB_PASSWORD`, `STUDYJARVIS_SERVER_SECRET_KEY`.

See [deployment & config](diagrams/deployment.md) for the full picture.

## Cross-cutting concerns (server)

- **Auth** — `app.before("/secure/*", ...)` installs [AuthorizationHandler](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/AuthorizationHandler.java), which validates the `Authorization: Bearer <jwt>` header via [JWTUtil](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/JWTUtil.java) (class is named `JwtUtil`) and stashes `username` as a Javalin context attribute. A second filter on `/secure/admin/*` adds an `isAdministrator` check via [AdminAuthorizationHandler](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/AdminAuthorizationHandler.java).
- **HandlerDecorator** — every handler is wrapped in [HandlerDecorator](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/HandlerDecorator.java), which short-circuits if an earlier `before` filter already set a non-200 status. This is how auth rejections stop the chain without each handler having to check.
- **OpenAPI** — `@OpenApi` annotations on every handler generate a spec at `/api/openapi`, rendered as ReDoc at `/api/docs`.

See [server components](diagrams/server-components.md).

## Per-user isolation

When a user logs in, [SessionWriter.createSession](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/SessionWriter.java) creates a row in `sessions` with two freshly-minted temp folders: `uploaded_files_path` and `extract_folder`. Subsequent file uploads land in that user's `uploaded_files_path`; [PrepareFilesHandler](../src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/PrepareFilesHandler.java) extracts into `extract_folder`, pushes the results into the GCS bucket under `user <userId>:`, and then clears both local folders. Jarvis handlers construct `Jarvis.getInstance(userId)` so the bucket handle uses the same prefix — keeping each user's context isolated at the Vertex AI layer.

See [data model](diagrams/data-model.md).

## Diagram index

- [Context](diagrams/context.md) — external actors and systems
- [Containers](diagrams/containers.md) — CLI process, server process, database, bucket, internal modules
- [Server components](diagrams/server-components.md) — handlers, filters, and the route table
- [CLI commands](diagrams/cli-commands.md) — the command class hierarchy and their names/shortcuts
- [Sequence — ask a question](diagrams/sequence-ask-question.md) — HTTP upload → extract → GCS → Gemini → response
- [Sequence — login & JWT](diagrams/sequence-auth.md) — login, authenticated calls, logout
- [Sequence — interactive quiz](diagrams/sequence-interactive-quiz.md) — CLI quiz loop
- [Data model](diagrams/data-model.md) — `users` and `sessions` tables
- [Deployment & config](diagrams/deployment.md) — runtime layout and configuration sources
