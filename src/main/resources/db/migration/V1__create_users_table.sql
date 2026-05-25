CREATE TYPE gender_enum AS ENUM (
    'MALE',
    'FEMALE',
    'OTHER'
);

CREATE TYPE user_status_enum AS ENUM (
    'NONE',
    'ACTIVE',
    'INACTIVE'
);

CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,

    gender gender_enum NOT NULL,

    birthday DATE NOT NULL,

    email VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    username VARCHAR(255) NOT NULL,

    password VARCHAR(255) NOT NULL,

    status user_status_enum NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_users_email UNIQUE(email),
    CONSTRAINT uk_users_phone UNIQUE(phone),
    CONSTRAINT uk_users_username UNIQUE(username)
);

CREATE INDEX idx_users_email
    ON users(email);

CREATE INDEX idx_users_username
    ON users(username);

CREATE INDEX idx_users_status
    ON users(status);