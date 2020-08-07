package com.example.mqlearn.controller;

import com.example.mqlearn.config.RabbitConfig;
import com.example.mqlearn.sender.HelloSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DeadController {

    @Autowired
    private HelloSender helloSender;

    @GetMapping("/testDead")
    public String testDead() {
        helloSender.testDead(RabbitConfig.deadExchangeName, RabbitConfig.deadRoutingKey);
        return "success";
    }



    @GetMapping("/testDead2")
    public String testDead2() {
        helloSender.testDead(RabbitConfig.deadExchangeName, RabbitConfig.deadRoutingKey);
        return "success";
    }

}
