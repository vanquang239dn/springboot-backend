CREATE TYPE gender AS ENUM (
    'MALE',
    'FEMALE',
    'OTHER'
);

CREATE TYPE user_type AS ENUM (
    'OWNER',
    'ADMIN',
    'USER'
);

CREATE TYPE user_status AS ENUM (
    'NONE',
    'ACTIVE',
    'INACTIVE'
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,

    gender gender NOT NULL,

    birthday DATE NOT NULL,

    email VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    username VARCHAR(255) NOT NULL,

    password VARCHAR(255) NOT NULL,

    type user_type NOT NULL,
    status user_status NOT NULL,

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