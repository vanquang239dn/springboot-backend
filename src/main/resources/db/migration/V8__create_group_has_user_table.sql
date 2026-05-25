CREATE TABLE group_has_user (

    group_user_id BIGSERIAL PRIMARY KEY,

    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_group_has_user_group_user
        UNIQUE(group_id, user_id),

    CONSTRAINT fk_group_has_user_group
        FOREIGN KEY(group_id)
        REFERENCES groups(group_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_group_has_user_user
        FOREIGN KEY(user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_group_has_user_group_id
    ON group_has_user(group_id);

CREATE INDEX idx_group_has_user_user_id
    ON group_has_user(user_id);