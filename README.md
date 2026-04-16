# StudyJarvis

StudyJarvis is a Java study-aid application that turns lecture material (PDFs and PowerPoints) into study artifacts — comprehensive notes, study guides, key points, quizzes, and an interactive Q&A — using Google's Gemini models through Vertex AI.

It runs in two modes that share the same core pipeline:

- **CLI** — an interactive shell (`Main`) driven by a typed command loop. Useful for single-user, local exploration.
- **HTTP server** — a Javalin app (`StudyJarvisServer`) with JWT-authenticated endpoints and PostgreSQL-backed users/sessions. Useful for multi-user deployments.

Both modes do the same three things under the hood:

1. Extract text/images from uploaded PDFs and PowerPoints.
2. Upload the extracted content to a Google Cloud Storage bucket.
3. Feed the bucket URIs into Vertex AI's Gemini model as multi-modal context and prompt it for the desired artifact.

## Entry points

- CLI: [src/main/java/com/christophertbarrerasconsulting/studyjarvis/Main.java](src/main/java/com/christophertbarrerasconsulting/studyjarvis/Main.java)
- Server: [src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/StudyJarvisServer.java](src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/StudyJarvisServer.java) (default port `7000`)
- Core orchestration: [Jarvis.java](src/main/java/com/christophertbarrerasconsulting/studyjarvis/Jarvis.java), [Gemini.java](src/main/java/com/christophertbarrerasconsulting/studyjarvis/Gemini.java), [GoogleBucket.java](src/main/java/com/christophertbarrerasconsulting/studyjarvis/GoogleBucket.java)

## External dependencies

- **Google Cloud Vertex AI** — Gemini model inference (`com.google.cloud:google-cloud-vertexai`)
- **Google Cloud Storage** — per-user staging of extracted content (`com.google.cloud:google-cloud-storage`)
- **PostgreSQL** — users, sessions, and per-session file paths ([Database.java](src/main/java/com/christophertbarrerasconsulting/studyjarvis/server/Database.java))
- **Apache PDFBox / POI** — PDF and PowerPoint extraction ([extraction/](src/main/java/com/christophertbarrerasconsulting/studyjarvis/extraction/))

## Configuration

Local properties file at:

- Windows: `%APPDATA%\studyjarvis.properties`
- macOS: `~/Library/Application Support/studyjarvis.properties`
- Linux: `~/.config/studyjarvis.properties`

Keys (see [AppSettings.java](src/main/java/com/christophertbarrerasconsulting/studyjarvis/file/AppSettings.java)): `BucketName`, `ExtractFolder`, `GeminiProjectId`, `GeminiModelName`, `GeminiLocation`.

Server-only environment variables:

- `STUDYJARVIS_DB_URL`, `STUDYJARVIS_DB_USER`, `STUDYJARVIS_DB_PASSWORD` — PostgreSQL connection
- `STUDYJARVIS_SERVER_SECRET_KEY` — HMAC key for JWT signing

Google Cloud credentials are picked up from the ambient environment (`GOOGLE_APPLICATION_CREDENTIALS` or `gcloud` application-default login).

## Build and run

```bash
./gradlew build                 # compile + unit tests
./gradlew functionalTest        # functional tests without GCP
./gradlew integrationTest       # full tests (requires GCP + Postgres)
```

Run the CLI:

```bash
./gradlew run -PmainClass=com.christophertbarrerasconsulting.studyjarvis.Main
```

Run the server (defaults to port 7000, serves ReDoc at `/api/docs`):

```bash
./gradlew run -PmainClass=com.christophertbarrerasconsulting.studyjarvis.server.StudyJarvisServer
```

## Documentation

- **[Architecture overview](docs/ARCHITECTURE.md)** — narrative walkthrough with embedded diagrams
- Diagrams:
  - [System context](docs/diagrams/context.md)
  - [Containers](docs/diagrams/containers.md)
  - [Server components](docs/diagrams/server-components.md)
  - [CLI commands](docs/diagrams/cli-commands.md)
  - [Sequence — ask a question](docs/diagrams/sequence-ask-question.md)
  - [Sequence — login & JWT](docs/diagrams/sequence-auth.md)
  - [Sequence — interactive quiz](docs/diagrams/sequence-interactive-quiz.md)
  - [Data model](docs/diagrams/data-model.md)
  - [Deployment & config](docs/diagrams/deployment.md)
