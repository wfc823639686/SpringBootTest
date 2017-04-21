package com.wfc.web.controller

import com.aliyun.openservices.ons.api.Message
import com.aliyun.openservices.ons.api.SendResult
import com.aliyun.openservices.ons.api.bean.ProducerBean
import com.wfc.web.common.RestClient
import com.wfc.web.common.utils.EncryptUtils
import com.wfc.web.common.utils.JSONUtils
import com.wfc.web.common.utils.UploadUtils
import com.wfc.web.model.EnterpriseFamous
import com.wfc.web.model.EnterpriseVideo
import com.wfc.web.model.QueryJobsModel
import com.wfc.web.rabbitmq.Sender
import com.wfc.web.service.CommonService
import com.wfc.web.service.EnterpriseService
import com.wfc.web.service.JobService
import com.wfc.web.service.UserService
//import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

import javax.servlet.http.HttpServletRequest

/**
 * Created by wangfengchen on 2016/11/21.
 */

@RestController
@RequestMapping("/test")
class TestController {

    @Autowired
    UserService userService
    @Autowired
    EnterpriseService enterpriseService
    @Autowired
    CommonService commonService
    @Autowired
    JobService jobService
    @Autowired
    StringRedisTemplate redisTemplate
    @Autowired
    Sender sender
    @Autowired
    ProducerBean producer

    @RequestMapping("/test")
    Object test(Integer id) {
        println('id ' + id)
        return userService.getUser(id)
    }

    @RequestMapping("/token")
    def token(String d, String timestamp, String r, String secret) {
        def token = "";
        token = EncryptUtils.MD5(r + timestamp + secret + d)
        token;
    }

    @RequestMapping("/cities")
    def cities() {
        commonService.cities()
    }

    @RequestMapping("/jobs")
    def jobs(QueryJobsModel q) {
        jobService.getJobs(q)
    }

    @RequestMapping("/getLabels")
    def getLabels(String key) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        List<String> list = listOperations.range(key, 0, listOperations.size(key));
        return list
    }

    @RequestMapping("/send")
    def send(String tag, String body) {
//        sender.send();
        def topic = "RAY_TOPIC_TEST"
        def map = ['uid': 3, 'body': body]
        Message msg = new Message(topic, tag, JSONUtils.toJson(map).bytes)
        SendResult sendResult = producer.send(msg)
        if (sendResult != null) {
            println(""+new Date() + " Send mq message success! Topic is:" + topic + " msgId is: " + sendResult.getMessageId())
        }
    }

    @RequestMapping("/http")
    def http() {
    }
}
