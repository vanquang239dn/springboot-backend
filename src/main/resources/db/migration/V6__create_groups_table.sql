CREATE TABLE groups (

    group_id BIGSERIAL PRIMARY KEY,

    group_name VARCHAR(255) NOT NULL,
    description VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_groups_group_name UNIQUE(group_name)
);

CREATE INDEX idx_groups_group_name
    ON groups(group_name);