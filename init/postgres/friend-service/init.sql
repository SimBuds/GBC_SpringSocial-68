-- Create the database separately
CREATE DATABASE friendsdb;

-- Connect to the database
\c friendsdb;

-- The rest of the commands can be in a transaction
BEGIN;

-- Create the user
CREATE USER myuser WITH PASSWORD 'mypass';

-- Grant privileges to the user
GRANT ALL PRIVILEGES ON DATABASE friendsdb TO myuser;

-- Create the friends table
CREATE TABLE IF NOT EXISTS friends (
                                       id SERIAL PRIMARY KEY,                 -- A unique ID for each record
                                       user_id VARCHAR(255) NOT NULL,         -- ID of the user
    friend_id VARCHAR(255) NOT NULL,       -- ID of the friend
    status VARCHAR(50) NOT NULL,           -- Status of the friendship (PENDING, ACCEPTED, REJECTED)
    created_at TIMESTAMP DEFAULT NOW(),    -- Timestamp of when the record was created
    updated_at TIMESTAMP DEFAULT NOW()     -- Timestamp of the last update
    );

-- Assuming you have a separate users table, you would create it as well
CREATE TABLE IF NOT EXISTS users (
                                     user_id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL
    -- Additional fields as necessary
    );

-- Insert some sample data into the users table (if needed)
INSERT INTO users (user_id, username, password, email, full_name)
VALUES
    ('user1', 'john_doe', 'password123', 'john.doe@example.com', 'John Doe'),
    ('user2', 'jane_doe', 'password456', 'jane.doe@example.com', 'Jane Doe');

-- Insert some sample data into the friends table
INSERT INTO friends (user_id, friend_id, status)
VALUES
    ('user1', 'user2', 'PENDING');

-- Commit the transaction
COMMIT;