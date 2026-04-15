# Sequence — Ask a Question (HTTP)

The end-to-end flow a client takes to ask Gemini a question about uploaded materials. Three requests are involved: upload, prepare, ask.

```mermaid
sequenceDiagram
    autonumber
    actor C as Client
    participant A as AuthorizationHandler
    participant U as UploadFilesHandler
    participant P as PrepareFilesHandler
    participant FH as FileHandler + PDF/PPTExtractor
    participant GB as GoogleBucket
    participant J as JarvisAskQuestionHandler
    participant Jar as Jarvis
    participant G as Gemini<br/>(VertexAI)
    participant DB as PostgreSQL<br/>(users, sessions)

    Note over C,A: Every /secure/* call runs AuthorizationHandler first

    %% Step 1 — upload
    C->>A: POST /secure/files<br/>Bearer JWT, multipart files
    A->>A: JwtUtil.validateToken<br/>ctx.attribute("username", ...)
    A->>U: forward
    U->>DB: UserReader.getUser(username)
    U->>DB: SessionReader.getSession(userId)
    U->>FH: copy each uploaded file into<br/>session.uploaded_files_path
    U-->>C: 201 Files uploaded successfully

    %% Step 2 — prepare
    C->>A: POST /secure/files/prepare<br/>Bearer JWT
    A->>P: forward (auth OK)
    P->>DB: UserReader + SessionReader
    P->>GB: countBucket() / clearBucket()<br/>for prefix "user <userId>:"
    P->>FH: extractFilesInDirectory(<br/>uploaded_files_path → extract_folder)
    FH->>FH: PDFExtractor / PowerPointExtractor
    P->>GB: uploadDirectoryContents(extract_folder)
    GB->>GB: write gs://<bucket>/user <id>:/...
    P->>FH: clearDirectory(extract_folder)<br/>clearDirectory(uploaded_files_path)
    P-->>C: 200 OK

    %% Step 3 — ask
    C->>A: POST /secure/jarvis/ask { question }
    A->>J: forward (auth OK)
    J->>DB: UserReader.getUser(username)
    J->>Jar: Jarvis.getInstance(userId)
    Jar->>GB: getURIs() for "user <id>:"
    Jar->>G: initializeMultiModalInput(uris)
    J->>Jar: askQuestion(question)
    Jar->>G: respond("Generate comprehensive notes...")
    G-->>Jar: notes text
    Jar->>G: textInput("Here are some notes:\n\n" + notes + "\n\n" + question)
    G-->>Jar: answer text
    Jar-->>J: answer
    J-->>C: 200 JSON(answer)
```

## Things worth knowing

- `AuthorizationHandler` uses Javalin's `before("/secure/*", ...)` filter, so it runs ahead of every handler in the diagram above. Every handler body then reads `ctx.attribute("username")`.
- `PrepareFilesHandler` clears the bucket prefix at the start of each prepare, so repeated preparations don't accumulate stale objects.
- `Jarvis.askQuestion` uses a two-hop prompting strategy: it first asks Gemini to generate notes on all topics, then injects those notes plus the user's question into a fresh `textInput` call. The first hop (`respond`) also appends the prompt+response into `Gemini.parts` for conversational continuity within the same Jarvis instance.
- `Jarvis` is `AutoCloseable`; each handler wraps it in try-with-resources so the `VertexAI` client is closed at the end of every request.
