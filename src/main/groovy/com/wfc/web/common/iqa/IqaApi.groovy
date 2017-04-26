package com.wfc.web.common.iqa

import com.wfc.web.common.RestClient
import org.springframework.beans.factory.annotation.Value
import javax.validation.constraints.NotNull

class IqaApi {

    static final String HOST = 'http://jisuznwd.market.alicloudapi.com'

    @Value("appcode")
    static String APP_CODE = ""


    static String question(@NotNull String question) {
        def path = '/iqa/query?question={question}'
        RestClient.get(HOST + path, question)
    }

    static String post() {
        def url = 'http://localhost:8080/api/user/insertFeedback.htm'
        Map<String, String> params =
                ['content': 'abc',
                 'uid'    : '234',
                 'type'   : '1']
        RestClient.post(url, params)
    }

    static void main(String[] args) {
//        print(question(null))
        post()
    }
}
