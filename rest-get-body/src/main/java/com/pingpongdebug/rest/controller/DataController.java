package com.pingpongdebug.rest.controller;

import com.pingpongdebug.rest.domain.InfoRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Suppose this is third party API -- offer GetMapping but require RequestBody
 */
@RestController
public class DataController {

    @GetMapping("/data/info")
    public String getInfo(@RequestBody InfoRequest request){

        return "Get: "+ request.toString();
    }

    @GetMapping("/version")
    public String getVersion(){

        return "Done ";
    }

}
