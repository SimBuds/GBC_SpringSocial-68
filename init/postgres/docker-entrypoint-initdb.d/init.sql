-- Create the database
CREATE DATABASE SpringSocialDB;

-- Create the user
CREATE USER myuser WITH PASSWORD 'mypass';

-- Grant privileges to the user
GRANT ALL PRIVILEGES ON DATABASE SpringSocialDB TO myuser;

-- Connect to the database
\c SpringSocialDB;

-- Create the tables

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(120) NOT NULL,
                       email VARCHAR(50) UNIQUE NOT NULL,
                       fullName VARCHAR(100)
);

CREATE TABLE comments (
                          id SERIAL PRIMARY KEY,
                          postId VARCHAR(255) NOT NULL,
                          content VARCHAR(250),
                          authorId VARCHAR(255) NOT NULL,
                          createdAt TIMESTAMP,
                          updatedAt TIMESTAMP
);

-- Insert some data into users table
INSERT INTO users (username, password, email, full_name) VALUES
    ('user1', 'password1', 'user1@example.com', 'User One'),
    ('user2', 'password2', 'user2@example.com', 'User Two');

-- Insert some data into comments table
INSERT INTO comments (postId, content, authorId, createdAt, updatedAt) VALUES
                      ('1', 'This is a comment', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
                      ('1', 'This is another comment', '2', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
                      ('2', 'This is a comment', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
                      ('2', 'This is another comment', '2', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
                      ('3', 'This is a comment', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00'),
                      ('3', 'This is another comment', '2', '2019-01-01 00:00:00', '2019-01-01 00:00:00');


