-- Create the initial administrator user.
--
-- IMPORTANT: Replace <REPLACE_WITH_BCRYPT_HASH> with a BCrypt hash of your chosen
-- password before running this against any real database. Generate the hash with
-- cost 10+, for example:
--
--   htpasswd -bnBC 10 "" 'your-password-here' | tr -d ':\n'
--
-- or programmatically with BCrypt.hashpw(password, BCrypt.gensalt(10)).
--
-- Do NOT commit a real hash. Do NOT deploy this file as-is.
INSERT INTO users (username, password_hash, is_administrator)
VALUES ('admin', '<REPLACE_WITH_BCRYPT_HASH>', true);
