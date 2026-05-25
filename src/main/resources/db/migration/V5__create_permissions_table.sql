CREATE TABLE permissions (

    permission_id BIGSERIAL PRIMARY KEY,

    permission VARCHAR(30) NOT NULL,
    description VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_permissions_permission UNIQUE(permission)
);

CREATE INDEX idx_permissions_permission
    ON permissions(permission);