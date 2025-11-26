-- Initial schema creation for Arconia Demo
-- V1__init.sql

CREATE TABLE IF NOT EXISTS books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    published_year INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for faster lookups
CREATE INDEX IF NOT EXISTS idx_books_author ON books(author);
CREATE INDEX IF NOT EXISTS idx_books_isbn ON books(isbn);

-- Insert sample data
INSERT INTO books (title, author, isbn, published_year) VALUES
    ('Clean Code', 'Robert C. Martin', '978-0132350884', 2008),
    ('Effective Java', 'Joshua Bloch', '978-0134685991', 2018),
    ('Spring in Action', 'Craig Walls', '978-1617294945', 2018),
    ('Designing Data-Intensive Applications', 'Martin Kleppmann', '978-1449373320', 2017);
