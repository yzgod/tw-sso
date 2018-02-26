package com.tongwei.auth.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.tongwei.auth.config.SsoRedisProperties.Cluster;
import com.tongwei.auth.config.SsoRedisProperties.Sentinel;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's Redis
 * support.
 *
 * @author Dave Syer
 * @author Andy Wilkinson
 * @author Christian Dupuis
 * @author Christoph Strobl
 * @author Phillip Webb
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Marco Aust
 */
@Configuration
@ConditionalOnClass({ JedisConnection.class, RedisOperations.class, Jedis.class })
@EnableConfigurationProperties(SsoRedisProperties.class)
public class SsoRedisAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SsoRedisAutoConfiguration.class);

    /**
     * Redis connection configuration.
     */
    @Configuration
    @ConditionalOnClass(GenericObjectPool.class)
    protected static class RedisConnectionConfiguration {

        private final SsoRedisProperties properties;

        private final RedisSentinelConfiguration sentinelConfiguration;

        private final RedisClusterConfiguration clusterConfiguration;

        public RedisConnectionConfiguration(SsoRedisProperties properties,
                ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
                ObjectProvider<RedisClusterConfiguration> clusterConfiguration) {
            this.properties = properties;
            this.sentinelConfiguration = sentinelConfiguration.getIfAvailable();
            this.clusterConfiguration = clusterConfiguration.getIfAvailable();
        }

        @Bean("ssoJedisConnectionFactory")
        @Primary
        public JedisConnectionFactory redisConnectionFactory() throws UnknownHostException {
            JedisConnectionFactory connectionFactory = applyProperties(createJedisConnectionFactory());
            String hostName = connectionFactory.getHostName();
            int port = connectionFactory.getPort();
            logger.info("认证中心redis连接地址: " + hostName + ":" + port);
            return connectionFactory;
        }

        protected final JedisConnectionFactory applyProperties(JedisConnectionFactory factory) {
            configureConnection(factory);
            if (this.properties.isSsl()) {
                factory.setUseSsl(true);
            }
            factory.setDatabase(this.properties.getDatabase());
            if (this.properties.getTimeout() > 0) {
                factory.setTimeout(this.properties.getTimeout());
            }
            return factory;
        }

        private void configureConnection(JedisConnectionFactory factory) {
            if (StringUtils.hasText(this.properties.getUrl())) {
                configureConnectionFromUrl(factory);
            } else {
                factory.setHostName(this.properties.getHost());
                factory.setPort(this.properties.getPort());
                if (this.properties.getPassword() != null) {
                    factory.setPassword(this.properties.getPassword());
                }
            }
        }

        private void configureConnectionFromUrl(JedisConnectionFactory factory) {
            String url = this.properties.getUrl();
            if (url.startsWith("rediss://")) {
                factory.setUseSsl(true);
            }
            try {
                URI uri = new URI(url);
                factory.setHostName(uri.getHost());
                factory.setPort(uri.getPort());
                if (uri.getUserInfo() != null) {
                    String password = uri.getUserInfo();
                    int index = password.lastIndexOf(":");
                    if (index >= 0) {
                        password = password.substring(index + 1);
                    }
                    factory.setPassword(password);
                }
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException("Malformed 'spring.redis.url' " + url, ex);
            }
        }

        protected final RedisSentinelConfiguration getSentinelConfig() {
            if (this.sentinelConfiguration != null) {
                return this.sentinelConfiguration;
            }
            Sentinel sentinelProperties = this.properties.getSentinel();
            if (sentinelProperties != null) {
                RedisSentinelConfiguration config = new RedisSentinelConfiguration();
                config.master(sentinelProperties.getMaster());
                config.setSentinels(createSentinels(sentinelProperties));
                return config;
            }
            return null;
        }

        /**
         * Create a {@link RedisClusterConfiguration} if necessary.
         * 
         * @return {@literal null} if no cluster settings are set.
         */
        protected final RedisClusterConfiguration getClusterConfiguration() {
            if (this.clusterConfiguration != null) {
                return this.clusterConfiguration;
            }
            if (this.properties.getCluster() == null) {
                return null;
            }
            Cluster clusterProperties = this.properties.getCluster();
            RedisClusterConfiguration config = new RedisClusterConfiguration(clusterProperties.getNodes());

            if (clusterProperties.getMaxRedirects() != null) {
                config.setMaxRedirects(clusterProperties.getMaxRedirects());
            }
            return config;
        }

        private List<RedisNode> createSentinels(Sentinel sentinel) {
            List<RedisNode> nodes = new ArrayList<RedisNode>();
            for (String node : StringUtils.commaDelimitedListToStringArray(sentinel.getNodes())) {
                try {
                    String[] parts = StringUtils.split(node, ":");
                    Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                    nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
                } catch (RuntimeException ex) {
                    throw new IllegalStateException("Invalid redis sentinel " + "property '" + node + "'", ex);
                }
            }
            return nodes;
        }

        private JedisConnectionFactory createJedisConnectionFactory() {
            JedisPoolConfig poolConfig = this.properties.getPool() != null ? jedisPoolConfig() : new JedisPoolConfig();

            if (getSentinelConfig() != null) {
                return new JedisConnectionFactory(getSentinelConfig(), poolConfig);
            }
            if (getClusterConfiguration() != null) {
                return new JedisConnectionFactory(getClusterConfiguration(), poolConfig);
            }
            return new JedisConnectionFactory(poolConfig);
        }

        private JedisPoolConfig jedisPoolConfig() {
            JedisPoolConfig config = new JedisPoolConfig();
            SsoRedisProperties.Pool props = this.properties.getPool();
            config.setMaxTotal(props.getMaxActive());
            config.setMaxIdle(props.getMaxIdle());
            config.setMinIdle(props.getMinIdle());
            config.setMaxWaitMillis(props.getMaxWait());
            return config;
        }

    }

    /**
     * Standard Redis configuration.
     */
    @Configuration
    protected static class RedisConfiguration {

        @Bean("ssoStringRedisTemplate")
        @Primary
        public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
                throws UnknownHostException {
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            logger.info("认证中心redis操作beanName : ssoStringRedisTemplate");
            return template;
        }

    }

}
