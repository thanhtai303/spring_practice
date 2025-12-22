CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100),
    password VARCHAR(255),
    status VARCHAR(20),
    delete_flag CHAR(1) DEFAULT 'N' NOT NULL,
    create_by VARCHAR(50),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_by VARCHAR(50),
    update_time TIMESTAMP
);

CREATE INDEX idx_users_username ON users (username);