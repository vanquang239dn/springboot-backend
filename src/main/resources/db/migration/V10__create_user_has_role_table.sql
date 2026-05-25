CREATE TABLE user_has_role (

    user_role_id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_user_has_role_user_role
        UNIQUE(user_id, role_id),

    CONSTRAINT fk_user_has_role_user
        FOREIGN KEY(user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_has_role_role
        FOREIGN KEY(role_id)
        REFERENCES roles(role_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_user_has_role_user_id
    ON user_has_role(user_id);

CREATE INDEX idx_user_has_role_role_id
    ON user_has_role(role_id);