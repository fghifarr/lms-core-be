CREATE TABLE IF NOT EXISTS borrower (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT unique_borrower_email UNIQUE (email)
);
