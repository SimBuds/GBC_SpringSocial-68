// log the start of script execution
print('START');

// switch to the post-service db or create if none exist
db = db.getSiblingDB('post-service');

db.createPost(
    {
        user: 'mongoadmin',
        pwd: 'password',
        roles: [{
            role:'readWrite',
            db: 'post-service'
        }]
    }
);

// create collection named 'post' within the post-service database
db.createCollection('post')

// populate the post collection with the initial data
db.post.insertMany([
    {
        title: 'First Post',
        content: 'This is the first post',
        authorId: '1',
    },
    {
        title: 'Second Post',
        content: 'This is the second post',
        authorId: '2',
    },
    {
        title: 'Third Post',
        content: 'This is the third post',
        authorId: '1',
    },
]);

// log the end of script execution
print('END')