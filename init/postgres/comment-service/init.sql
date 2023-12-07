-- Create the database separately
CREATE DATABASE commentdb;

-- Connect to the database
\c commentdb;

-- The rest of the commands can be in a transaction
BEGIN;

-- Create the user
CREATE USER myuser WITH PASSWORD 'mypass';

-- Grant privileges to the user
GRANT ALL PRIVILEGES ON DATABASE commentdb TO myuser;

-- Create the tables
CREATE TABLE IF NOT EXISTS comments (
                                        id SERIAL PRIMARY KEY,
                                        post_id VARCHAR(255) NOT NULL,
    content VARCHAR(250),
    author_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    );

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