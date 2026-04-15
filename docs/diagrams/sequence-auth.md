# Sequence — Login & JWT

How a user obtains and uses a JWT, how the server verifies it, and how logout tears down a session.

## Login

```mermaid
sequenceDiagram
    autonumber
    actor C as Client
    participant L as LoginHandler
    participant UR as UserReader
    participant BC as BCrypt
    participant JW as JwtUtil
    participant SW as SessionWriter
    participant FH as FileHandler
    participant DB as PostgreSQL

    C->>L: POST /login { username, password }
    L->>UR: getUser(username)
    UR->>DB: SELECT ... FROM users WHERE username = ?
    DB-->>UR: row / null
    UR-->>L: User or null

    alt invalid credentials
        L-->>C: 401 Invalid username or password
    else valid
        L->>BC: BCrypt.checkpw(password, storedHash)
        BC-->>L: true
        L->>SW: createSession(userId)
        SW->>FH: createNewTempFolder("upload_<id>")
        SW->>FH: createNewTempFolder("extract_<id>")
        SW->>DB: INSERT INTO sessions ... RETURNING session_id
        DB-->>SW: session_id
        SW-->>L: Session
        L->>JW: generateToken(username)
        JW-->>L: signed JWT (HS256, 24h expiry)
        L-->>C: 200 { username, isAdmin, token }
    end
```

## Authenticated call

```mermaid
sequenceDiagram
    autonumber
    actor C as Client
    participant F as AuthorizationHandler<br/>(before /secure/*)
    participant Fa as AdminAuthorizationHandler<br/>(before /secure/admin/*)
    participant H as Target handler
    participant JW as JwtUtil
    participant UR as UserReader

    C->>F: any /secure/* request<br/>Authorization: Bearer <token>
    F->>JW: validateToken(token)
    alt token invalid
        JW-->>F: null
        F-->>C: 401 Unauthorized
    else token valid
        JW-->>F: username
        F->>F: ctx.attribute("username", username)
        opt path is /secure/admin/*
            F->>Fa: continue to admin filter
            Fa->>UR: getUser(username)
            alt not administrator
                Fa-->>C: 403 Forbidden
            end
        end
        F->>H: decorated handler runs<br/>(HandlerDecorator skips if status != 200)
        H-->>C: 2xx response
    end
```

## Logout

```mermaid
sequenceDiagram
    autonumber
    actor C as Client
    participant Lo as LogoutHandler
    participant SW as SessionWriter
    participant FH as FileHandler
    participant DB as PostgreSQL

    C->>Lo: POST /logout { username }
    Lo->>SW: deleteSessions(username)
    SW->>FH: delete uploaded_files_path and extract_folder<br/>for each session
    SW->>DB: DELETE FROM sessions WHERE user_id = ?
    DB-->>SW: rows deleted
    SW-->>Lo: count
    Lo-->>C: 200
```

## Things worth knowing

- JWT signing uses HMAC256 with `STUDYJARVIS_SERVER_SECRET_KEY` from the environment. If that env var is unset, the server will still start but every generated token will fail to verify — configure it before running.
- The token carries only `username` as a claim. `AdminAuthorizationHandler` re-queries the database on every admin request to get the `is_administrator` bit; role changes take effect immediately.
- `JwtUtil` is the class name in code even though the file is `JWTUtil.java`.
- Session rows are created **on login, not on upload**. A user who never logs in again has no session and therefore no upload/extract directories.
