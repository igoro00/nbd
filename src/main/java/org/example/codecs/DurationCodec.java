package org.example.codecs;

import org.bson.*;
import org.bson.codecs.*;

import java.time.Duration;

public class DurationCodec implements Codec<Duration> {
    @Override
    public void encode(BsonWriter writer, Duration value, EncoderContext encoderContext) {
        writer.writeInt64(value.toMillis());
    }

    @Override
    public Duration decode(BsonReader reader, DecoderContext decoderContext) {
        long millis;
        BsonType currentType = reader.getCurrentBsonType();
        
        if (currentType == BsonType.INT64) {
            millis = reader.readInt64();
        } else if (currentType == BsonType.INT32) {
            millis = reader.readInt32();
        } else {
            throw new IllegalArgumentException("Expected INT32 or INT64 for Duration, got " + currentType);
        }
        
        return Duration.ofMillis(millis);
    }

    @Override
    public Class<Duration> getEncoderClass() {
        return Duration.class;
    }
}