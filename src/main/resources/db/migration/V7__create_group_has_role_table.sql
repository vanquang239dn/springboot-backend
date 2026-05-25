CREATE TABLE group_has_role (

    group_role_id BIGSERIAL PRIMARY KEY,

    group_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_group_has_role_group_role
        UNIQUE(group_id, role_id),

    CONSTRAINT fk_group_has_role_group
        FOREIGN KEY(group_id)
        REFERENCES groups(group_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_group_has_role_role
        FOREIGN KEY(role_id)
        REFERENCES roles(role_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_group_has_role_group_id
    ON group_has_role(group_id);

CREATE INDEX idx_group_has_role_role_id
    ON group_has_role(role_id);