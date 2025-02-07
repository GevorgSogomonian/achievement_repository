package faang.school.achievement.config;

import faang.school.achievement.listener.LikeEventListener;
import faang.school.achievement.listener.ProfilePicEventListener;
import faang.school.achievement.listener.PostEventListener;
import faang.school.achievement.listener.CommentAchievementListener;
import faang.school.achievement.listener.ProfilePicEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.achievement}")
    public String achievementChannel;

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannel;

    @Value("${spring.data.redis.channel.post}")
    private String postChannel;

    @Value("${spring.data.redis.channel.profile-picture}")
    private String profilePicture;

    @Value("${spring.data.redis.channel.album}")
    private String albumTopicName;

    @Value("${spring.data.redis.topic.likeChannel}")
    public String likeChannel;

    @Value("${spring.data.redis.channel.comment_achievement}")
    private String commentChannel;

    @Value("${spring.data.redis.channel.mentorship}")
    private String mentorshipChannelTopicName;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }

    @Bean
    public MessageListenerAdapter profilePictureListener(ProfilePicEventListener profilePicEventListener) {
        return new MessageListenerAdapter(profilePicEventListener);
    }

    @Bean
    public MessageListenerAdapter postListener(PostEventListener postEventListener) {
        return new MessageListenerAdapter(postEventListener);
    }

    @Bean
    public MessageListenerAdapter commentListener(CommentAchievementListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(achievementChannel);
    }

    @Bean("postChannelTopic")
    public ChannelTopic postChannelTopic() {
        return new ChannelTopic(postChannel);
    }

    @Bean
    public ChannelTopic profilePictureTopic() {
        return new ChannelTopic(profilePicture);
    }

    @Bean("albumChannelTopic")
    public ChannelTopic albumChannelTopic() {
        return new ChannelTopic(albumTopicName);
    }

    @Bean
    public ChannelTopic mentorshipTopic() {
        return new ChannelTopic(mentorshipChannelTopicName);
    }

    @Bean
    public ChannelTopic likeEventTopic() {
        return new ChannelTopic(likeChannel);
    }

    @Bean
    public MessageListenerAdapter likeEventListenerAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic commentAchievementChannel() {
        return new ChannelTopic(commentChannel);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                 ProfilePicEventListener profilePicEventListener,
                                                 PostEventListener postEventListener,
                                                 LikeEventListener likeEventListener,
                                                 CommentAchievementListener commentEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(profilePictureListener(profilePicEventListener), profilePictureTopic());
        container.addMessageListener(postListener(postEventListener), postChannelTopic());
        container.addMessageListener(likeEventListenerAdapter(likeEventListener), likeEventTopic());
        container.addMessageListener(commentListener(commentEventListener), commentAchievementChannel());

        return container;
    }
}