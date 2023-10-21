-- Create the database separately
CREATE DATABASE servicesdb;

-- Connect to the database
\c servicesdb;

-- The rest of the commands can be in a transaction
BEGIN;

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
    full_name VARCHAR(100)
    );

CREATE TABLE IF NOT EXISTS comments (
                                        id SERIAL PRIMARY KEY,
                                        post_id VARCHAR(255) NOT NULL,
    content VARCHAR(250),
    author_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    );

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