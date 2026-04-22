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

Run the server (defaults to port 7000, serves ReDoc at `/api/docs`) — this is the default `mainClass`:

```bash
./gradlew run
```

Run the CLI — override `mainClass` via a project property. In PowerShell, quote the value so dots aren't parsed as member access:

```powershell
./gradlew run "-PmainClass=com.christophertbarrerasconsulting.studyjarvis.Main"
```

```bash
./gradlew run -PmainClass=com.christophertbarrerasconsulting.studyjarvis.Main
```

## Local dev (backend + webapp together)

The Angular webapp lives in a sibling repo ([studyjarviswebapp-tjb](../studyjarviswebapp-tjb/studyjarviswebapp)) and calls the backend at `http://localhost:7000`. To run both with one command on Windows:

1. One-time setup — make sure the prereqs above are in place (Postgres running, `gcloud auth application-default login`, `studyjarvis.properties` populated), then:

   ```powershell
   Copy-Item dev.env.example.ps1 dev.env.ps1
   # edit dev.env.ps1 with your DB password and a JWT secret
   cd ..\..\studyjarviswebapp-tjb\studyjarviswebapp
   npm install
   ```

2. Every run, from the backend repo root:

   ```powershell
   .\dev.ps1
   ```

   Two PowerShell windows open — backend on `:7000`, webapp on `:4200`. Close either window to stop that service.

   `dev.env.ps1` is gitignored. Requires PowerShell 7+ (`pwsh`).

## Running tests in VS Code

When you open the backend folder in VS Code, accept the prompt to install the recommended extensions (**Extension Pack for Java** and **Gradle for Java**).

**Unit tests** (`src/test`) — no env vars needed. Open any test class and use the **Run Test** / **Debug Test** CodeLens above a `@Test` method, or the flask-icon Test Explorer sidebar.

**Functional / integration tests** (`src/functional`, `src/integration`) — the VS Code Test Explorer does not reliably discover these custom source sets, so run them via Gradle and attach the debugger:

1. Open a VS Code terminal and source env vars:

   ```powershell
   . .\dev.env.ps1
   ```

2. Run the Gradle task with JVM debug enabled:

   ```powershell
   .\gradlew functionalTest --debug-jvm        # or integrationTest
   ```

   Gradle prints `Listening for transport dt_socket at address: 5005` and pauses.

3. In **Run and Debug** (`Ctrl+Shift+D`), pick **Attach to Gradle Test (port 5005)** and press F5. Tests execute; breakpoints hit.

To run tests without the debugger, drop `--debug-jvm`, or use the Gradle extension's task panel (studyjarvis → Tasks → verification → `functionalTest` / `integrationTest`).

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
