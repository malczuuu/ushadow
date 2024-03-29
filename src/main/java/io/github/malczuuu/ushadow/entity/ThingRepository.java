package io.github.malczuuu.ushadow.entity;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThingRepository extends MongoRepository<ThingEntity, ObjectId> {

  Optional<ThingEntity> findByUid(String uid);

  void deleteByUid(String id);

  boolean existsByUid(String id);
}
