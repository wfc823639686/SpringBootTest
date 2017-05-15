package com.wfc.web.common.aliyun

class OSSConstants {

    static String ACCESS_KEY

    static String SECRET_KEY

    static String RES_BUCKET_NAME
    static String IMG_BUCKET_NAME
    static String VIDEO_BUCKET_NAME

    final static String ENDPOINT = "http://oss-cn-qingdao.aliyuncs.com"

    static void debug(String a, String s) {
        ACCESS_KEY = a
        SECRET_KEY = s
        RES_BUCKET_NAME = "ssb-resource-debug"
        IMG_BUCKET_NAME = "ssb-img-debug"
        VIDEO_BUCKET_NAME = "ssb-video-debug"
    }

    static void release(String a, String s) {
        ACCESS_KEY = a
        SECRET_KEY = s
        RES_BUCKET_NAME = "ssb-resource"
        IMG_BUCKET_NAME = "ssb-img"
        VIDEO_BUCKET_NAME = "ssb-video"
    }

}
