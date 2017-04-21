package com.wfc.web.common.iqa

import com.wfc.web.common.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.support.HttpRequestWrapper
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

import javax.validation.constraints.NotNull

/**
 * Created by wangfengchen on 2017/4/20.
 */
class IqaApi {

    static final String HOST = 'http://jisuznwd.market.alicloudapi.com'

    @Value("appcode")
    static String APP_CODE = "b064284e5f5f42c9a8c1213abab3d474"



    static String question(@NotNull String question) {
        def path = '/iqa/query?question={question}'
        RestClient.get(HOST + path, question)
    }

    static String post() {
        def url = 'http://localhost:8080/api/user/insertFeedback.htm'
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put('content', 'abc')
        params.put('uid', '234')
        params.put('type', '1')
        RestClient.post(url, params)
    }

    static void main(String[] args) {
//        print(question(null))
        post()
    }
}
