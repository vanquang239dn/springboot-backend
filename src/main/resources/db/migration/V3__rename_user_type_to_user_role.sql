ALTER TABLE users
RENAME COLUMN type TO role;

ALTER TYPE gender RENAME TO gender_enum;

ALTER TYPE user_type RENAME TO user_role_enum;

ALTER TYPE user_status RENAME TO user_status_enum;