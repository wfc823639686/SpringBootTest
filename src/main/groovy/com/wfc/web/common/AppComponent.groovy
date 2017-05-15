package com.wfc.web.common

import com.wfc.web.common.aliyun.OSSApi
import com.wfc.web.common.aliyun.OSSConstants
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Created by wangfengchen on 2017/4/25.
 */
@Component
class AppComponent {

    @Value('${aliyun.accesskeys}')
    String key
    @Value('${aliyun.secret}')
    String secret
    @Value('${spring.profiles.active}')
    String active;


    @PostConstruct
    void init() {
        //oss init
        if (StringUtils.equals("pro", active)) {
            println('!!! active is pro')
            OSSConstants.release(key, secret)
        } else {
            println('!!! active is test')
            OSSConstants.debug(key, secret)
        }
    }

}
