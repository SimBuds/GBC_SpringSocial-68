-- Start a transaction
BEGIN;

CREATE DATABASE servicesdb;

-- Connect to the database
\c servicesdb;

-- Create the user
CREATE USER myuser WITH PASSWORD 'mypass';

-- Grant privileges to the user
GRANT ALL PRIVILEGES ON DATABASE servicesdb TO myuser;

-- Create the tables
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL, -- Consider hashing the password
    email VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) -- Changed to snake_case
    );

CREATE TABLE IF NOT EXISTS comments (
                                        id SERIAL PRIMARY KEY,
                                        post_id VARCHAR(255) NOT NULL, -- Changed to snake_case
    content VARCHAR(250),
    author_id VARCHAR(255) NOT NULL, -- Changed to snake_case
    created_at TIMESTAMP, -- Changed to snake_case
    updated_at TIMESTAMP -- Changed to snake_case
    );

-- Update comments to set default values for null columns (if any exist)
UPDATE comments SET author_id = 'default' WHERE author_id IS NULL; -- Changed to snake_case
UPDATE comments SET post_id = 'default' WHERE post_id IS NULL; -- Changed to snake_case
ALTER TABLE comments RENAME COLUMN authorid TO author_id;
ALTER TABLE comments RENAME COLUMN postid TO post_id;

-- Insert some data into users table
INSERT INTO users (username, password, email, full_name)
VALUES
    ('user1', 'password1', 'user1@example.com', 'Casey Hsu'),
    ('user2', 'password2', 'user2@example.com', 'Matt Price');

-- Insert some data into comments table
INSERT INTO comments (post_id, content, author_id, created_at, updated_at)
VALUES
    ('1', 'This is a comment', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
    ('1', 'This is another comment', '2', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
    ('2', 'This is a comment', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
    ('2', 'This is another comment', '2', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
    ('3', 'This is a comment', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
    ('3', 'This is another comment', '2', '2019-01-01 00:00:00', '2019-01-01 00:00:00');

-- Commit the transaction
COMMIT;