package com.wfc.web.rabbitmq

import com.wfc.web.service.UserService
//import org.springframework.amqp.rabbit.annotation.Exchange
//import org.springframework.amqp.rabbit.annotation.Queue
//import org.springframework.amqp.rabbit.annotation.QueueBinding
//import org.springframework.amqp.rabbit.annotation.RabbitHandler
//import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by wangfengchen on 2017/3/31.
 */
@Component
class Receiver {

//    @Autowired
//    UserService userService
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "test.score", durable = "true", autoDelete = "false"),
//            exchange = @Exchange(value = "test.score", durable = "true", autoDelete = "false"),
//            key = "test.score"
//    ))
//    void process(Map rev) {
//        System.out.println("Receiver : " + rev.uid);
//        System.out.println("Receiver : " + rev.score);
//        System.out.println("Receiver : " + rev.type);
//        System.out.println("Receiver : " + rev.time);
//
//    }
}
