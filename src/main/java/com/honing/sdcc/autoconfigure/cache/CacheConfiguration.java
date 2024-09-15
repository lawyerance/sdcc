package com.honing.sdcc.autoconfigure.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class CacheConfiguration {


	private static final Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);

	@Bean
	@ConditionalOnProperty(value = "spring.cache.type", havingValue = "redis")
	public RedisCacheConfiguration redisCacheConfiguration(ObjectProvider<Jackson2ObjectMapperBuilder> jackson2ObjectMapperBuilder, CacheProperties cacheProperties) {
		Jackson2ObjectMapperBuilder available =
			jackson2ObjectMapperBuilder.getIfAvailable(Jackson2ObjectMapperBuilder::json);
		return creaetRedisCacheConfiguration(cacheProperties, available);
	}


	static RedisCacheConfiguration creaetRedisCacheConfiguration(CacheProperties cacheProperties,
																 Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
		logger.info("Using customized {}", RedisCacheConfiguration.class);
		CacheProperties.Redis redisProperties = cacheProperties.getRedis();
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
//		cacheConfiguration.entryTtl(Duration.ofSeconds(10));
		config =
			config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(jackson2ObjectMapperBuilder.build(), Object.class)));
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(redisProperties.getTimeToLive());
		}
		if (redisProperties.getKeyPrefix() != null) {
			config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
		}
		if (!redisProperties.isCacheNullValues()) {
			config = config.disableCachingNullValues();
		}
		if (!redisProperties.isUseKeyPrefix()) {
			config = config.disableKeyPrefix();
		}
		return config;
	}
}
