# Containers

The runtime units inside StudyJarvis and the shared modules they use.

```mermaid
flowchart TB
    subgraph clients[Clients]
        student([Student])
        apiClient([API client])
    end

    subgraph cli_proc[CLI process — Main]
        cmdSession[CommandSession]
        cmdParser[CommandParser]
        commands[Command subclasses<br/>ask-question, create-notes,<br/>create-quiz, ...]
    end

    subgraph server_proc[HTTP server process — StudyJarvisServer]
        javalin[Javalin app<br/>:7000]
        authFilter[AuthorizationHandler<br/>AdminAuthorizationHandler]
        handlers[Handlers<br/>Login/Logout, Users, Sessions,<br/>Upload/Prepare, Jarvis *]
        openapi[OpenAPI + ReDoc<br/>/api/docs]
    end

    subgraph shared[Shared modules]
        jarvis[Jarvis<br/>orchestrator]
        gemini[Gemini<br/>Vertex AI client]
        bucket[GoogleBucket<br/>GCS wrapper]
        extract[extraction/<br/>PDFExtractor, PowerPointExtractor]
        fileHelpers[file/<br/>AppSettings, FileHandler, ConfigReader]
        quiz[quiz/<br/>InteractiveQuiz + runner]
    end

    subgraph stores[External stores]
        gcs[(Google Cloud Storage)]
        vertex[(Vertex AI / Gemini)]
        pg[(PostgreSQL)]
        props[(studyjarvis.properties<br/>on local disk)]
        tmp[(OS temp dirs<br/>per-session upload/extract)]
    end

    student --> cmdSession
    apiClient -->|HTTPS + JWT| javalin
    javalin --> authFilter --> handlers
    javalin --> openapi

    cmdSession --> cmdParser --> commands --> jarvis
    handlers --> jarvis
    handlers -->|upload / prepare| extract
    handlers -->|read/write| pg
    handlers -->|staging| tmp

    jarvis --> gemini --> vertex
    jarvis --> bucket --> gcs
    commands --> quiz
    fileHelpers --> props
    commands --> fileHelpers
    handlers --> fileHelpers
```

## Responsibilities at a glance

| Container | Role |
| --- | --- |
| **CLI process** | Interactive shell; single-user; uses the local properties file for all settings. |
| **HTTP server process** | Multi-user Javalin app; adds JWT auth, PostgreSQL, and per-user GCS prefixes on top of the shared pipeline. |
| **Jarvis / Gemini / GoogleBucket** | Shared orchestration; both modes call the exact same methods here. |
| **extraction/** | Turns PDFs and PowerPoints into the files that eventually get uploaded to GCS. |
| **file/** | Config loading and local filesystem helpers (used by both modes). |
| **quiz/** | Holds quiz state between prompts; the CLI runs it interactively, the server returns the first generation as JSON. |
