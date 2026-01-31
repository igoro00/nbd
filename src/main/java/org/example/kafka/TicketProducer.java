package org.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.bson.types.ObjectId;

public class TicketProducer {
    private KafkaProducer<ObjectId, String> producer;
}
