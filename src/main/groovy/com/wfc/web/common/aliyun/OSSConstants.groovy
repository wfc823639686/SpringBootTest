package com.wfc.web.common.aliyun

import org.springframework.beans.factory.annotation.Value

/**
 * Created by wangfengchen on 2016/11/24.
 */
class OSSConstants {

    @Value('${my.aliyun.accesskeys}')
    static String KEY
    @Value('${my.aliyun.secret}')
    static String SECRET

    final static String RES_BUCKET_NAME = "ssb-resource"
    final static String IMG_BUCKET_NAME = "ssb-img"
    final static String VIDEO_BUCKET_NAME = "ssb-video"

    final static String ENDPOINT = "http://oss-cn-qingdao.aliyuncs.com"

    final static String DOMAIN = "shangshaban.com"

    final static String IMG_URL = "http://" + IMG_BUCKET_NAME + "." + DOMAIN
    final static String VIDEO_URL = "http://" + VIDEO_BUCKET_NAME + "." + DOMAIN
    final static String RES_URL = "http://" + RES_BUCKET_NAME + "." + DOMAIN

}
