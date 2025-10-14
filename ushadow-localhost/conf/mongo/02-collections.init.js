db = db.getSiblingDB("ushadow");

db.createCollection("shadows");
db.createCollection("things");
db.createCollection("violations");

db.runCommand({
  createIndexes: "shadows",
  indexes: [{
    "name": "thingUid_unique",
    "key": { "thingUid": 1 },
    "unique": true
  }]
});

db.runCommand({
  createIndexes: "things",
  indexes: [{
    "name": "uid_unique",
    "key": { "uid": 1 },
    "unique": true
  }]
});

db.runCommand({
  createIndexes: "violations",
  indexes: [{
    "name": "thingUid__id",
    "key": { "thingUid": 1, "_id": 1 }
  }]
});
