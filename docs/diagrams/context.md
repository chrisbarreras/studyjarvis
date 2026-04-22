# System Context

StudyJarvis from the outside — who uses it and what it depends on.

```mermaid
flowchart LR
    student([Student / CLI user])
    apiClient([API Client<br/>e.g. frontend or script])
    admin([Administrator])

    subgraph sj[StudyJarvis]
        direction TB
        cli[CLI<br/>Main]
        server[HTTP server<br/>StudyJarvisServer]
    end

    gemini[(Google Vertex AI<br/>Gemini model)]
    gcs[(Google Cloud Storage<br/>bucket)]
    pg[(PostgreSQL<br/>users &amp; sessions)]
    fs[(Local filesystem<br/>uploads, extracts, config)]

    student -->|types commands| cli
    apiClient -->|JWT-authenticated<br/>HTTPS| server
    admin -->|admin endpoints| server

    cli -->|upload &amp; prompt| gcs
    cli -->|generate content| gemini
    cli -->|read/write| fs

    server -->|upload &amp; prompt| gcs
    server -->|generate content| gemini
    server -->|users, sessions| pg
    server -->|staging files| fs
```

## Notes

- The CLI is a single-user shell — it doesn't touch PostgreSQL and uses a default "user -1" GCS prefix.
- The server partitions every GCS object under a `user <userId>:` prefix so multiple users share one bucket safely.
- Both modes read the same `studyjarvis.properties` file to learn the bucket name and Gemini project/model/location.
