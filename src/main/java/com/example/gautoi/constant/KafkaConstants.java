package com.example.gautoi.constant;

public final class KafkaConstants {
    public static final String PERSON_TOPIC = "person-events";
    public static final String GROUP_ID = "person-service-group";
    public static final String GROUP_KAFKA_FACTORY = "personKafkaListenerContainerFactory";

//    tax TaxCalculationEvent
    public static final String TAX_CALCULATION_TOPIC = "tax-calculation-events";
    public static final String TAX_GROUP = "tax-calculation-service-group";
    public static final String TAX_KAFKA_FACTORY = "taxKafkaListenerContainerFactory";
}
