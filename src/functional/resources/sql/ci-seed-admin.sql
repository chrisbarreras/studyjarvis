-- Test-only admin seed for CI.
-- Creates an admin/password user so functional tests that log in as "admin/password"
-- (see Client.login() in src/functional/java/.../server/Client.java) can run against
-- the ephemeral CI Postgres container.
--
-- DO NOT run this against any non-ephemeral database. Production setup should use
-- src/main/sql/6-create-admin-user.sql with a BCrypt hash you generate yourself.
INSERT INTO users (username, password_hash, is_administrator)
VALUES ('admin', '$2a$10$vDL2a.LiSszA4cvpTxUkFOkbjQoI3xwgTWHSmQlKh5MSjV.0PwJYy', true);
