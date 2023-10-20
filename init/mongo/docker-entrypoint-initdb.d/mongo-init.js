// log the start of script execution
print('START');

// mongo-init.js

db.createUser({
    user: "mongoadmin",
    pwd: "mypass",
    roles: [
        {
            role: "readWrite",
            db: "testdb"
        }
    ]
});

// Use the database
db = db.getSiblingDB('SpringSocialMongo');

// Insert sample posts
db.post.insertMany([
    {
        "title": "Sample Post 1",
        "content": "This is the content of the first sample post.",
        "authorId": "author123",
        "createdAt": ISODate("2023-10-19T12:00:00Z"),
        "updatedAt": ISODate("2023-10-19T12:00:00Z")
    },
    {
        "title": "Sample Post 2",
        "content": "This is the content of the second sample post.",
        "authorId": "author124",
        "createdAt": ISODate("2023-10-20T12:00:00Z"),
        "updatedAt": ISODate("2023-10-20T12:00:00Z")
    }
]);

// log the end of script execution
print('END')