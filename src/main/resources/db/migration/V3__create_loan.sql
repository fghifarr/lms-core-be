CREATE TABLE IF NOT EXISTS loan (
    id BIGSERIAL PRIMARY KEY,
    borrower_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrowed_at TIMESTAMP NOT NULL,
    returned_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES book(id),
    CONSTRAINT fk_loan_borrower FOREIGN KEY (borrower_id) REFERENCES borrower(id)
);

CREATE INDEX idx_loan_book_id_returned_at ON loan(book_id, returned_at);
