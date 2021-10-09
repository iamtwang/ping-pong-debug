package com.pingpongdebug.rest.controller;

import com.pingpongdebug.rest.domain.InfoRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.function.Function;

@RestController
public class HelloController {

    @Resource(name = "RestTemplate")
    private RestTemplate restTemplate;

    @GetMapping("/hello/{name}")
    public String getInfo(@PathVariable String name){

        HttpEntity entity = nameToInfoReq.andThen(infoRequestHttpEntity).apply(name);
        /** calling external API */
        ResponseEntity<String> info =
                restTemplate.exchange("http://localhost:1919/data/info", HttpMethod.GET, entity,String.class);
        return info.getBody();
    }

    private Function<String, InfoRequest> nameToInfoReq = name -> {
        InfoRequest request = new InfoRequest();
        request.setName(name);
        request.setAge(10);
        return  request;
    };

    private Function<InfoRequest, HttpEntity<InfoRequest>> infoRequestHttpEntity = request ->
            new HttpEntity<>(request);
}
