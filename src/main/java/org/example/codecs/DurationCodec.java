package org.example.codecs;

import com.datastax.oss.driver.api.core.data.CqlDuration;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.nio.ByteBuffer;
import java.time.Duration;

public class DurationCodec implements TypeCodec<Duration> {

    @NonNull
    @Override
    public GenericType<Duration> getJavaType() {
        return GenericType.DURATION;
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.DURATION;
    }

    @Nullable
    @Override
    public ByteBuffer encode(@Nullable Duration value, @NonNull com.datastax.oss.driver.api.core.ProtocolVersion protocolVersion) {
        if (value == null) return null;
        CqlDuration cqlDuration = CqlDuration.newInstance(0, 0, value.toNanos());
        return TypeCodecs.DURATION.encode(cqlDuration, protocolVersion);
    }

    @Nullable
    @Override
    public Duration decode(@Nullable ByteBuffer bytes, @NonNull com.datastax.oss.driver.api.core.ProtocolVersion protocolVersion) {
        CqlDuration cqlDuration = TypeCodecs.DURATION.decode(bytes, protocolVersion);
        if (cqlDuration == null) return null;
        return Duration.ofNanos(cqlDuration.getNanoseconds());
    }

    @NonNull
    @Override
    public String format(@Nullable Duration value) {
        return (value == null) ? "NULL" : value.toString();
    }

    @Nullable
    @Override
    public Duration parse(@Nullable String value) {
        return (value == null || value.equalsIgnoreCase("NULL")) ? null : Duration.parse(value);
    }
}