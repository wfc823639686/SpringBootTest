package com.wfc.web.common.utils

import java.security.MessageDigest

/**
 * Created by wangfengchen on 2016/11/28.
 */
class EncryptUtils {

    /**
     * MD5加密
     * @param s
     * @return
     */
    public final static String MD5(String s) {
        def hexDigits = ['0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f']
        if(s == null || s == ""){
            return ""
        }
        try {
            byte[] btInput = s.getBytes()
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            mdInst.update(btInput)
            // 获得密文
            byte[] md = mdInst.digest()
            // 把密文转换成十六进制的字符串形式
            int j = md.length
            def str = new char[j * 2]
            int k = 0
            for (def i = 0; i < j; i++) {
                byte byte0 = md[i]
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]
                str[k++] = hexDigits[byte0 & 0xf]
            }
            return new String(str)
        } catch (Exception e) {
            e.printStackTrace()
            return null
        }
    }
}
