package com.learnkafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.Item;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ItemDeserializer implements Deserializer<Item> {
    private static final Logger logger = LoggerFactory.getLogger(ItemDeserializer.class);

    @Override
    public Item deserialize(String topic, byte[] data) {
        logger.info("Inside deserialize");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, Item.class);
        } catch (IOException e) {
            logger.error("IOException in deserialize ", e);
            return null;
        }
    }
}
