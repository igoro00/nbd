package org.example.codecs;

import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

public class CodecRegistryFactory {
    public static CodecRegistry getCodecRegistry() {
        return CodecRegistries.fromProviders(
                new CustomCodecProvider(),
                MongoClientSettings.getDefaultCodecRegistry(),
                PojoCodecProvider.builder()
                        .automatic(true)
                        .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                        .build()
        );
    }
}
