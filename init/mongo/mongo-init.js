// log the start of script execution
print('START');

// This will create a new database or switch to it if it already exists
db = db.getSiblingDB('SpringSocialMongo');

// Create a user with readWrite permissions on the SpringSocialMongo database
db.createUser({
    user: "mongoadmin",
    pwd: "mypass",
    roles: [
        {
            role: "readWrite",
            db: "SpringSocialMongo"
        }
    ]
});

// Create the 'post' collection if it doesn't exist, as it will be used below
db.createCollection('postdb');

// Define a function to get the current date and time
function NOW() {
    return new Date();
}

// Insert sample posts into the 'post' collection
db.post.insertMany([
    {
        "title": "Sample Post 1",
        "content": "This is the content of the first sample post.",
        "authorId": "author123",
        "createdAt": NOW(),
        "updatedAt": NOW()
    },
    {
        "title": "Sample Post 2",
        "content": "This is the content of the second sample post.",
        "authorId": "author124",
        "createdAt": NOW(),
        "updatedAt": NOW()
    }
]);

// log the end of script execution
print('END');
