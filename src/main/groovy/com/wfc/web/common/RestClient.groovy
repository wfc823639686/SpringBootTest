package com.wfc.web.common

import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.support.HttpRequestWrapper
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

class RestClient {

    static RestTemplate restTemplate

    static {
        ClientHttpRequestInterceptor headerInterceptor =
                new AcceptHeaderHttpRequestInterceptor()
        restTemplate = new RestTemplate()
        restTemplate.setInterceptors(Collections.singletonList(headerInterceptor))
    }

    static Map post(String url, Map<String, String> params) {
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>()
        mvm.setAll(params)
        restTemplate.postForObject(url, mvm, Map.class)
    }

    static Map get(String url, String... urlVariables) {
        restTemplate.getForObject(url, Map.class, urlVariables)
    }

    static Map get(String url, Map<String, String> urlVariables) {
        restTemplate.getForObject(url, Map.class, urlVariables)
    }

    static class AcceptHeaderHttpRequestInterceptor implements ClientHttpRequestInterceptor {

        @Override
        ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
            HttpRequestWrapper requestWrapper = new HttpRequestWrapper(httpRequest);
            requestWrapper.getHeaders().add('Authorization', 'APPCODE ' + APP_CODE)
            return clientHttpRequestExecution.execute(requestWrapper, bytes)
        }
    }

}
