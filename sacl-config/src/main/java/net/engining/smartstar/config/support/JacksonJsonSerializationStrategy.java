package net.engining.smartstar.config.support;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;

/**
 * @author Eric Lu
 * @date 2019-11-28 20:25
 **/
public class JacksonJsonSerializationStrategy extends StandardStringSerializationStrategy {

    private static final FastJsonRedisSerializer OBJECT_SERIALIZER = new FastJsonRedisSerializer(Object.class);

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        return (T) OBJECT_SERIALIZER.deserialize(bytes);
    }

    @Override
    protected byte[] serializeInternal(Object object) {
        return OBJECT_SERIALIZER.serialize(object);
    }
}
