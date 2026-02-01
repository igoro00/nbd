package org.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.example.codecs.CodecRegistryFactory;
import org.example.codecs.ObjectIdSerializer;
import org.example.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class TicketProducer implements AutoCloseable {
    private final KafkaProducer<ObjectId, String> producer;
    private final CodecRegistry codecRegistry = CodecRegistryFactory.getCodecRegistry();

    private static final Logger logger = LoggerFactory.getLogger(TicketProducer.class);

    public TicketProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ObjectIdSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        producer = new KafkaProducer<>(props);
    }

    String toJSON(Ticket ticket) {
        BsonDocument bsonDocument = new BsonDocument();
        EncoderContext encoderContext = EncoderContext.builder().build();
        Codec<Ticket> codec = codecRegistry.get(Ticket.class);
        try (BsonDocumentWriter writer = new BsonDocumentWriter(bsonDocument)) {
            codec.encode(writer, ticket, encoderContext);
            return bsonDocument.toJson();
        }
    }

    public void send(Ticket ticket) {
        try{
            String json = toJSON(ticket);
            ProducerRecord<ObjectId, String> record = new ProducerRecord<>(KafkaConfig.TOPIC, ticket.getEntityId(), json);
            RecordMetadata meta = producer.send(record).get();
            logger.info("Produced ticket {} on partition {}", ticket.getEntityId(), meta.partition());
        } catch (Exception e) {
            logger.error("Error while producing ticket {}: {}", ticket.getEntityId(), String.valueOf(e));
        }
    }

    @Override
    public void close() {
        producer.close();
    }
}
