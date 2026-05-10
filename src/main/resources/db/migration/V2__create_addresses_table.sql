CREATE TABLE addresses (

    id BIGSERIAL PRIMARY KEY,

    apartment_number VARCHAR(255),
    floor VARCHAR(255),
    building VARCHAR(255),

    street_number VARCHAR(255),
    street VARCHAR(255),

    city VARCHAR(255),
    country VARCHAR(255),

    address_type VARCHAR(255),

    user_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_addresses_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_addresses_user_id
    ON addresses(user_id);

CREATE INDEX idx_addresses_city
    ON addresses(city);

CREATE INDEX idx_addresses_country
    ON addresses(country);