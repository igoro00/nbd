package org.example.codecs;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.time.Duration;

public class CustomCodecProvider implements CodecProvider {

    public CustomCodecProvider() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Duration.class) {
            return (Codec<T>) new DurationCodec();
        }
        // return null when there is no provider for the requested class
        return null;
    }
}