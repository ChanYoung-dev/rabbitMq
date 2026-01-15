package com.example.hellomessagequeue;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloMessageQueueApplication {
  public static void main(String[] args) {
    SpringApplication.run(HelloMessageQueueApplication.class, args);
  }

}
