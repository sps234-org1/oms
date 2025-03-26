package com.jocata.oms.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class InventoryTopicConfig {

    @Bean
    public NewTopic reserveInventoryTopic() {
        return TopicBuilder.name("reserve-inventory").build();
    }

    @Bean
    public NewTopic releaseInventoryTopic() {
        return TopicBuilder.name("release-inventory").build();
    }

    @Bean
    public NewTopic updateInventoryTopic() {
        return TopicBuilder.name("update-inventory").build();
    }

}
