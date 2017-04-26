package com.wfc.web.common

import com.wfc.web.common.aliyun.OSSApi
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

    @PostConstruct
    void init() {
        //oss init
        OSSApi.init(key, secret)
    }

}
