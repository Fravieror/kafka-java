package com.learnkafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.Item;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ItemProducer_Approach2 {

    private static final Logger logger = LoggerFactory.getLogger(ItemProducer_Approach2.class);
    ObjectMapper objectMapper = new ObjectMapper();
    String topicName = "items";
    KafkaProducer<Integer,String> kafkaProducer;

    public ItemProducer_Approach2(Map<String,Object> propsMap){

        kafkaProducer = new KafkaProducer<Integer, String>(propsMap);
    }

    Callback callback = (metadata, exception) -> {
        if(exception!=null){
            logger.error("Exception in Callback is {} ", exception.getMessage());
        }else{
            logger.info("Published Message Offset in callback is {} and the partition is {}", metadata.offset(), metadata.partition());
        }

    };

    public void close(){
        kafkaProducer.close();
    }
    public static Map<String,Object> propsMap(){

        Map<String,Object> propsMap  = new HashMap<>();
        propsMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        propsMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        propsMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        propsMap.put(ProducerConfig.ACKS_CONFIG, "all");
        propsMap.put(ProducerConfig.RETRIES_CONFIG, 10);
        propsMap.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 3000);
        return propsMap;
    }


    public void publishMessageSync(Item item) throws JsonProcessingException {
        String value = objectMapper.writeValueAsString(item);
        ProducerRecord<Integer,String> producerRecord = new ProducerRecord<>(topicName, item.getId(), value);

        try {
            RecordMetadata recordMetadata = kafkaProducer.send(producerRecord).get();
            //System.out.println("partition" + recordMetadata.partition() +" , offset : " + recordMetadata.offset());
            logger.info("Message {} sent successfully for the key {} ", value, item.getId());
            logger.info("Published Message Offset is {} and the partition is {}", recordMetadata.offset(), recordMetadata.partition());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            logger.error("Exception in publishMessageSync : {} ", e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ItemProducer_Approach2 messageProducer = new ItemProducer_Approach2(propsMap());
        Item item1 = new Item(1,"LG TV", 400.00);
        Item item2 = new Item(2,"Iphone Pro Max", 949.99);
        List.of(item1, item2)
                .forEach(item -> {
                    try {
                        messageProducer.publishMessageSync(item);
                    } catch (JsonProcessingException e) {
                        logger.error("JsonProcessingException in main ", e);
                    }
                });
        //messageProducer.publishMessageASync(null, "ABD-Async");
        Thread.sleep(3000);
    }
}
