// MongoDB initialization script
// This script runs when the MongoDB container is first created

db = db.getSiblingDB('issuehub');

// Create collections with validation
db.createCollection('users', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['username', 'email', 'password', 'role'],
      properties: {
        username: {
          bsonType: 'string',
          description: 'must be a string and is required'
        },
        email: {
          bsonType: 'string',
          pattern: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$',
          description: 'must be a valid email and is required'
        },
        role: {
          enum: ['EMPLOYEE', 'TECHNICIAN', 'MANAGER'],
          description: 'must be one of the enum values'
        }
      }
    }
  }
});

db.createCollection('issues');
db.createCollection('categories');
db.createCollection('notifications');
db.createCollection('comments');

// Create indexes for better performance
db.users.createIndex({ 'email': 1 }, { unique: true });
db.users.createIndex({ 'username': 1 }, { unique: true });
db.issues.createIndex({ 'createdBy': 1 });
db.issues.createIndex({ 'assignedTo': 1 });
db.issues.createIndex({ 'status': 1 });
db.issues.createIndex({ 'priority': 1 });
db.issues.createIndex({ 'category': 1 });
db.issues.createIndex({ 'createdAt': -1 });
db.notifications.createIndex({ 'userId': 1 });
db.notifications.createIndex({ 'read': 1 });
db.comments.createIndex({ 'issueId': 1 });

print('Database initialization completed successfully!');
