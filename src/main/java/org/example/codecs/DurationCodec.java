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
        long millis = reader.readInt64();
        return Duration.ofMillis(millis);
    }

    @Override
    public Class<Duration> getEncoderClass() {
        return Duration.class;
    }
}