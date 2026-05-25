CREATE TABLE roles (

    role_id BIGSERIAL PRIMARY KEY,

    role VARCHAR(30) NOT NULL,
    description VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_roles_role UNIQUE(role)
);

CREATE INDEX idx_roles_role
    ON roles(role);