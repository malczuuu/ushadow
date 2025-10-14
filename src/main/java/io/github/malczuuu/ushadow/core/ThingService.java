package io.github.malczuuu.ushadow.core;

import io.github.malczuuu.ushadow.core.exception.ThingNotFoundException;
import io.github.malczuuu.ushadow.core.mapper.ThingMapper;
import io.github.malczuuu.ushadow.entity.ShadowRepository;
import io.github.malczuuu.ushadow.entity.ThingEntity;
import io.github.malczuuu.ushadow.entity.ThingRepository;
import io.github.malczuuu.ushadow.entity.ViolationRepository;
import io.github.malczuuu.ushadow.model.CreateThingModel;
import io.github.malczuuu.ushadow.model.PasswordModel;
import io.github.malczuuu.ushadow.model.ThingModel;
import io.github.malczuuu.ushadow.model.ThingPageModel;
import io.github.malczuuu.ushadow.model.UpdateThingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ThingService {

  private final ThingRepository thingRepository;
  private final ShadowRepository shadowRepository;
  private final ViolationRepository violationRepository;

  private final ThingMapper mapper = new ThingMapper();

  public ThingService(
      ThingRepository thingRepository,
      ShadowRepository shadowRepository,
      ViolationRepository violationRepository) {
    this.shadowRepository = shadowRepository;
    this.thingRepository = thingRepository;
    this.violationRepository = violationRepository;
  }

  public ThingPageModel findThings(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<ThingEntity> entities = thingRepository.findAll(pageable);
    return mapper.toModel(entities);
  }

  public void requireThing(String id) {
    if (!doesThingExists(id)) {
      throw new ThingNotFoundException(id);
    }
  }

  public boolean doesThingExists(String id) {
    return thingRepository.existsByUid(id);
  }

  public ThingModel findThing(String id) {
    ThingEntity entity = fetchThing(id);
    return mapper.toModel(entity);
  }

  private ThingEntity fetchThing(String uid) {
    return thingRepository.findByUid(uid).orElseThrow(() -> new ThingNotFoundException(uid));
  }

  public ThingModel createThing(CreateThingModel thing) {
    ThingEntity entity = new ThingEntity(thing.getId(), thing.getName(), thing.isEnabled());
    entity = thingRepository.save(entity);
    return mapper.toModel(entity);
  }

  public ThingModel updateThing(String id, UpdateThingModel thing) {
    ThingEntity entity = fetchThing(id);
    entity.setName(thing.getName());
    entity.setEnabled(thing.isEnabled());
    entity.setVersion(thing.getVersion());
    entity = thingRepository.save(entity);
    return mapper.toModel(entity);
  }

  public void updatePassword(String id, PasswordModel password) {
    ThingEntity entity = fetchThing(id);
    entity.setPassword(password.getPassword());
    entity.setVersion(password.getVersion());
    entity = thingRepository.save(entity);
    thingRepository.save(entity);
  }

  public void deleteThing(String id) {
    violationRepository.deleteAllByThingUid(id);
    shadowRepository.deleteByThingUid(id);
    thingRepository.deleteByUid(id);
  }
}
