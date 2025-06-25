package com.example.gautoi.integration.kafka;
import com.example.gautoi.constant.KafkaConstants;
import com.example.gautoi.entity.PersonEvent;
import com.example.gautoi.handler.PersonServiceKafkaHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonConsumerService {
    private final PersonServiceKafkaHandler personServiceKafkaHandler;
    @KafkaListener(topics= KafkaConstants.PERSON_TOPIC, groupId = KafkaConstants.GROUP_ID, containerFactory = KafkaConstants.GROUP_KAFKA_FACTORY  )
    public void consumePersonService(PersonEvent person) {
            switch (person.getEventType()){
                case CREATE -> personServiceKafkaHandler.handleCreatePersonEvent(person);
                case UPDATE -> personServiceKafkaHandler.handleUpdatePersonEvent(person);
                case DELETE -> personServiceKafkaHandler.handleDeletePersonEvent(person);
                default -> throw new IllegalArgumentException("Unexpected value: " + person.getEventType());
            }
    }

}
