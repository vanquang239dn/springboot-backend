CREATE TABLE role_has_permission (

    role_permission_id BIGSERIAL PRIMARY KEY,

    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_role_has_permission_role_permission
        UNIQUE(role_id, permission_id),

    CONSTRAINT fk_role_has_permission_role
        FOREIGN KEY(role_id)
        REFERENCES roles(role_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_role_has_permission_permission
        FOREIGN KEY(permission_id)
        REFERENCES permissions(permission_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_role_has_permission_role_id
    ON role_has_permission(role_id);

CREATE INDEX idx_role_has_permission_permission_id
    ON role_has_permission(permission_id);