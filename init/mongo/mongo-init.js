print('START');

db = db.getSiblingDB('SpringSocialMongo');

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

db.createCollection('posts');

function NOW() {
    return new Date();
}

db.posts.insertMany([
    {
        "title": "Sample Post 1",
        "content": "This is the content of the first sample post.",
        "authorId": "user1",
        "createdAt": NOW(),
        "updatedAt": NOW()
    },
    {
        "title": "Sample Post 2",
        "content": "This is the content of the second sample post.",
        "authorId": "user2",
        "createdAt": NOW(),
        "updatedAt": NOW()
    }
]);

print('END');