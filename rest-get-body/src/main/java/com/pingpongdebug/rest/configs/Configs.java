package com.pingpongdebug.rest.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Configs {

    @Bean("RestTemplate")
    public RestTemplate restTemplate(){
        /**
         * This one use @see org.springframework.http.client.SimpleClientHttpRequestFactory
         * <code>
         *     prepareConnection
         *
         *     boolean mayWrite =
         * 				("POST".equals(httpMethod) || "PUT".equals(httpMethod) ||
         * 						"PATCH".equals(httpMethod) || "DELETE".equals(httpMethod));
         *
         * 		connection.setDoOutput(mayWrite);
         *
         * </code>
         * So client will get 400.
         * [{"timestamp":"2021-10-09T21:43:50.729+00:00","status":400,"error":"Bad Request","path":"/data/info"}]
         */
        RestTemplate template = new RestTemplate();
        return template;
    }

}
