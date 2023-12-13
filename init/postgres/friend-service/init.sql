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

-- Commit the transaction
COMMIT;