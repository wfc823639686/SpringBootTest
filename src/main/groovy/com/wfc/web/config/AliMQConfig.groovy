package com.wfc.web.config

import com.aliyun.openservices.ons.api.MessageListener
import com.aliyun.openservices.ons.api.PropertyKeyConst
import com.aliyun.openservices.ons.api.bean.ConsumerBean
import com.aliyun.openservices.ons.api.bean.ProducerBean
import com.aliyun.openservices.ons.api.bean.Subscription
import com.wfc.web.alimq.MessageListenerImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * Created by wangfengchen on 2017/4/12.
 */
@Configuration
class AliMQConfig {

    @Value('${my.aliyun.accesskeys}')
    String ACCESS_KEY
    @Value('${my.aliyun.secret}')
    String SECRET_KEY
    String ONSADDR = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet"

    @Autowired
    MessageListener messageListener

    @Bean
    Subscription subscription() {
        return new Subscription(["topic": "RAY_TOPIC_TEST", "expression": "*"])
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    ProducerBean producer() {
        ProducerBean producerBean = new ProducerBean();
        Properties producerProperties = new Properties();
        producerProperties.setProperty(PropertyKeyConst.ProducerId, "PID_RAY_TEST")
        producerProperties.setProperty(PropertyKeyConst.AccessKey, ACCESS_KEY)
        producerProperties.setProperty(PropertyKeyConst.SecretKey, SECRET_KEY)
        producerProperties.setProperty(PropertyKeyConst.ONSAddr, ONSADDR)
        producerBean.setProperties(producerProperties)
        return producerBean
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    ConsumerBean consumer(Subscription subscription) {
        ConsumerBean consumerBean = new ConsumerBean();
        Properties producerProperties = new Properties();
        producerProperties.setProperty(PropertyKeyConst.ConsumerId, "CID_RAY_TEST")
        producerProperties.setProperty(PropertyKeyConst.AccessKey, ACCESS_KEY)
        producerProperties.setProperty(PropertyKeyConst.SecretKey, SECRET_KEY)
        producerProperties.setProperty(PropertyKeyConst.ONSAddr, ONSADDR)
        consumerBean.setProperties(producerProperties)
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>()
        subscriptionTable.put(subscription, messageListener)
        consumerBean.setSubscriptionTable(subscriptionTable)
        return consumerBean
    }

}
