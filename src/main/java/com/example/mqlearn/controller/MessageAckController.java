package com.example.mqlearn.controller;

import com.example.mqlearn.config.RabbitConfig;
import com.example.mqlearn.sender.HelloSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MessageAckController {

    @Autowired
    private HelloSender helloSender;

    @GetMapping("/testMessageAck")
    public String testMessageAck() {
        helloSender.send(null, RabbitConfig.queueName);
        return "success";
    }

}
