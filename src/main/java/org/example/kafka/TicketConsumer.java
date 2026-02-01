package org.example.kafka;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.example.codecs.CodecRegistryFactory;
import org.example.codecs.ObjectIdDeserializer;
import org.example.managers.TicketManager;
import org.example.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class TicketConsumer implements AutoCloseable{
    private final CodecRegistry codecRegistry = CodecRegistryFactory.getCodecRegistry();

    private static final Logger logger = LoggerFactory.getLogger(TicketConsumer.class);

    private final KafkaConsumer<ObjectId, String> consumer;
    private final TicketManager ticketManager;
    private final String id;

    @Getter
    private Thread thread;

    public TicketConsumer(String id, TicketManager ticketManager) {
        this.id = id;
        this.ticketManager = ticketManager;

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConfig.GROUP_ID);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, id);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ObjectIdDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumer = new KafkaConsumer<>(props);
        logger.info("[{}] constructed", id);
    }

    public void start() {
        logger.info("[{}] starting", id);
        consumer.subscribe(List.of(KafkaConfig.TOPIC));
        consumer.poll(Duration.ZERO);

        logger.info("[{}] starting thread", id);
        this.thread = new Thread(this::consume);
        thread.start();
    }

    @Override
    public void close() throws InterruptedException {
        consumer.wakeup();
        if (thread != null) {
            thread.join();
        }
        consumer.unsubscribe();
    }

    public Ticket fromJSON(String json){
        BsonDocument bsonDocument = BsonDocument.parse(json);
        Codec<Ticket> codec = codecRegistry.get(Ticket.class);

        try (BsonDocumentReader reader = new BsonDocumentReader(bsonDocument)) {
            return codec.decode(reader, DecoderContext.builder().build());
        }
    }

    public void consume(){
        try {
            while (true) {
                ConsumerRecords<ObjectId, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<ObjectId, String> record : records) {
                    Ticket ticket = fromJSON(record.value());
                    try{
                        ticketManager.createTicket(ticket);
                    } catch (Exception e){
                        logger.error(String.valueOf(e));
                    }
                    Set<TopicPartition> assignment = consumer.assignment();
                    logger.info("[{}] Saved ticket {} from partition {} (assigned partitions: {})", id, ticket.getEntityId(), record.partition(), assignment);
                    consumer.commitSync();
                }
            }
        } catch (WakeupException e) {
            logger.info("[{}] woke up. Closing...", id);
        }
    }
}
