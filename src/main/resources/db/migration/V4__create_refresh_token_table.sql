CREATE TABLE refresh_tokens (

    id UUID PRIMARY KEY,

    user_id BIGINT NOT NULL,

    jwt_id VARCHAR(255) NOT NULL UNIQUE,
    session_id VARCHAR(255) NOT NULL,

    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    revoke_reason VARCHAR(255),
    revoked_at TIMESTAMP,

    issued_at TIMESTAMP NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
);

CREATE INDEX idx_refresh_tokens_user_id
    ON refresh_tokens(user_id);

CREATE INDEX idx_refresh_tokens_expired_at
    ON refresh_tokens(expired_at);

CREATE INDEX idx_refresh_tokens_session_id
    ON refresh_tokens(session_id);