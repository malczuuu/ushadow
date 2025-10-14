package io.github.malczuuu.ushadow.core;

import io.github.malczuuu.ushadow.configuration.RabbitConfiguration;
import io.github.malczuuu.ushadow.core.mapper.ShadowMapper;
import io.github.malczuuu.ushadow.entity.ShadowEntity;
import io.github.malczuuu.ushadow.entity.ShadowRepository;
import io.github.malczuuu.ushadow.model.ShadowModel;
import io.github.malczuuu.ushadow.model.UpdateDesiredModel;
import io.github.malczuuu.ushadow.model.message.ShadowEnvelope;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.stereotype.Service;

@Service
public class ShadowService {

  private final ShadowRepository shadowRepository;
  private final RabbitOperations rabbitOperations;

  private final ShadowMapper mapper = new ShadowMapper();

  public ShadowService(ShadowRepository shadowRepository, RabbitOperations rabbitOperations) {
    this.shadowRepository = shadowRepository;
    this.rabbitOperations = rabbitOperations;
  }

  public ShadowModel findShadow(String thingId) {
    ShadowEntity shadow = fetchThing(thingId);
    return mapper.toModel(shadow);
  }

  public ShadowModel updateShadow(String thingId, UpdateDesiredModel update) {
    ShadowEntity shadow = fetchThing(thingId);
    shadow.setDesired(new HashMap<>(update.getDesired()));
    shadow.setVersion(update.getVersion());

    shadow = shadowRepository.save(shadow);

    ShadowModel model = mapper.toModel(shadow);
    rabbitOperations.convertAndSend(
        RabbitConfiguration.TOPIC_EXCHANGE,
        RabbitConfiguration.shadowMessageTopic(thingId),
        new ShadowEnvelope(model, UUID.randomUUID().toString()));

    return model;
  }

  private ShadowEntity fetchThing(String thingUid) {
    return shadowRepository
        .findByThingUid(thingUid)
        .orElseGet(() -> shadowRepository.save(new ShadowEntity(thingUid)));
  }

  public ShadowModel updateShadow(String thingId, Map<String, Object> reported) {
    ShadowEntity shadow = fetchThing(thingId);
    shadow.setReported(new HashMap<>(reported));
    shadow = shadowRepository.save(shadow);
    return mapper.toModel(shadow);
  }
}
