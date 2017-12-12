CREATE TABLE reset_password_tokens (
  id         BIGSERIAL PRIMARY KEY NOT NULL,
  user_id    BIGINT                NOT NULL,
  created_at TIMESTAMP             NOT NULL,
  expires_at TIMESTAMP             NOT NULL,
  nonce      BIGINT                NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX reset_password_tokens_nonce_unique_index
  ON reset_password_tokens (nonce);
