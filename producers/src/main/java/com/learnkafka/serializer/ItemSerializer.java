package com.learnkafka.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.Item;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemSerializer implements Serializer<Item> {
    private static final Logger logger = LoggerFactory.getLogger(ItemSerializer.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Item item) {
        logger.info("Inside Serialization Logic");
        try {
            return objectMapper.writeValueAsBytes(item);
        } catch (JsonProcessingException e) {
            logger.info("JsonProcessingException in  serialize : {} ",item,  e);
            return null;
        }

    }
}
