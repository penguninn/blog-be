db = db.getSiblingDB('blog');

db.createCollection('roles');

db.roles.insertMany([
  { name: 'ROLE_USER' },
  { name: 'ROLE_ADMIN' }
]);

// db.roles.createIndex({ "name": 1 }, { unique: true });