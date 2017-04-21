package com.wfc.web.alimq

import com.aliyun.openservices.ons.api.Action
import com.aliyun.openservices.ons.api.ConsumeContext
import com.aliyun.openservices.ons.api.Message
import com.aliyun.openservices.ons.api.MessageListener
import com.wfc.web.common.utils.JSONUtils
import org.springframework.stereotype.Component

/**
 * Created by wangfengchen on 2017/4/12.
 */
@Component("myConsumer")
class MyConsumer implements MessageListener {

    @Override
    Action consume(Message message, ConsumeContext consumeContext) {
        println("" + new Date() + " Receive message, Topic is:" +
                message.getTopic() + ", MsgId is:" + message.getMsgID())

        println(message.getTag())

        switch (message.getTag()) {
            case 'insert':
                println('insert')
                break
            case 'update':
                println('update')
                break
        }
        Map map = JSONUtils.toObject(new String(message.getBody()), HashMap.class)
        println(map.uid);

        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage
    }
}
