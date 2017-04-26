package com.wfc.web.config

import com.aliyun.openservices.ons.api.MessageListener
import com.aliyun.openservices.ons.api.PropertyKeyConst
import com.aliyun.openservices.ons.api.bean.ConsumerBean
import com.aliyun.openservices.ons.api.bean.ProducerBean
import com.aliyun.openservices.ons.api.bean.Subscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.annotation.Resource


/**
 * Created by wangfengchen on 2017/4/12.
 */
@Configuration
class AliMQConfig {

    @Value('${aliyun.accesskeys}')
    String ACCESS_KEY
    @Value('${aliyun.secret}')
    String SECRET_KEY
    String ONSADDR = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet"

    final static String USER_TOPIC = "TOPIC_USER"
    final static String USER_PRODUCER_ID = "PID_USER"
    final static String USER_CONSUMER_ID = "CID_USER"

    @Resource(name = "myConsumer")
    MessageListener myConsumer

    @Bean
    Properties producerProperties() {
        Properties producerProperties = new Properties();
        producerProperties.setProperty(PropertyKeyConst.AccessKey, ACCESS_KEY)
        producerProperties.setProperty(PropertyKeyConst.SecretKey, SECRET_KEY)
        producerProperties.setProperty(PropertyKeyConst.ONSAddr, ONSADDR)
        return producerProperties
    }

    @Bean
    Properties consumerProperties() {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, ACCESS_KEY)
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, SECRET_KEY)
        consumerProperties.setProperty(PropertyKeyConst.ONSAddr, ONSADDR)
        return consumerProperties
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    ProducerBean producer(Properties producerProperties) {
        ProducerBean producerBean = new ProducerBean()
        producerProperties.setProperty(PropertyKeyConst.ProducerId, "PID_RAY_TEST")
        producerBean.setProperties(producerProperties)
        return producerBean
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    ConsumerBean consumer(Properties consumerProperties) {
        ConsumerBean consumerBean = new ConsumerBean()
        consumerProperties.setProperty(PropertyKeyConst.ConsumerId, "CID_RAY_TEST")
        consumerBean.setProperties(consumerProperties)

        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>()
        Subscription subscription = new Subscription(["topic": "RAY_TOPIC_TEST", "expression": "*"])
        subscriptionTable.put(subscription, myConsumer)
        consumerBean.setSubscriptionTable(subscriptionTable)
        return consumerBean
    }

}
