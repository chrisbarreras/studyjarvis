# Security

## Required environment variables

The HTTP server will not function without all of the following set in its process environment:

| Variable | Purpose |
| --- | --- |
| `STUDYJARVIS_SERVER_SECRET_KEY` | HMAC key for signing JWTs. Use a long random string (generate with [SecretKeyGenerator](src/main/java/com/christophertbarrerasconsulting/studyjarvis/utils/SecretKeyGenerator.java) or `openssl rand -base64 48`). |
| `STUDYJARVIS_DB_URL` | JDBC URL for PostgreSQL, e.g. `jdbc:postgresql://db.internal:5432/studyjarvis`. |
| `STUDYJARVIS_DB_USER` | Postgres user. |
| `STUDYJARVIS_DB_PASSWORD` | Postgres password. |
| `GOOGLE_APPLICATION_CREDENTIALS` | Path to a GCP service-account JSON with Vertex AI + GCS permissions, **or** use `gcloud auth application-default login`. |

None of these may be committed. Store them in your deployment's secret manager (GCP Secret Manager, AWS Secrets Manager, HashiCorp Vault, etc.) and inject at runtime.

## Bootstrapping the first admin user

1. Choose a strong password for the initial administrator.
2. Generate a BCrypt hash of it (cost 10 or higher):
   ```bash
   htpasswd -bnBC 10 "" 'your-strong-password' | tr -d ':\n'
   ```
   or use `BCrypt.hashpw(password, BCrypt.gensalt(10))` from `org.mindrot:jbcrypt`.
3. Open [`src/main/sql/6-create-admin-user.sql`](src/main/sql/6-create-admin-user.sql), replace `<REPLACE_WITH_BCRYPT_HASH>` with the hash, and run the file against your database.
4. **Do not commit the filled-in file.** The committed version must always contain the placeholder.

## Test fixtures

Functional tests log in with the fixture credentials `admin` / `password`. The seed that creates that user lives at [`src/functional/resources/sql/ci-seed-admin.sql`](src/functional/resources/sql/ci-seed-admin.sql) and is **only** run by the CI workflow against its ephemeral PostgreSQL container. Do not run that seed against any long-lived database, and do not point functional tests at shared infrastructure.

## Reporting vulnerabilities

Open a GitHub issue for non-sensitive reports, or contact the repo owner directly for anything you'd rather not disclose publicly.
