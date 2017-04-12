package com.wfc.web.rabbitmq

//import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by wangfengchen on 2017/3/31.
 */
@Component
class Sender {

//    @Autowired
//    private AmqpTemplate rabbitTemplate
//
//    public void send() {
//        def context = ["uid": 3, "score": 10, "type": 1]
//        System.out.println("Sender : " + context)
//        this.rabbitTemplate.convertAndSend("test.score", "test.score", context)
//    }
}
