package com.example.hellomessagequeue.step8_1;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

  private final RabbitTemplate rabbitTemplate;
  private final RetryTemplate retryTemplate;

  public OrderConsumer(RabbitTemplate rabbitTemplate, RetryTemplate retryTemplate) {
    this.rabbitTemplate = rabbitTemplate;
    this.retryTemplate = retryTemplate;
  }

  @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETED_QUEUE)
  public void consume(String message) {
    retryTemplate.execute(context -> {
      try {
        System.out.println("# 리시브 메시지 : " + message + " # retry : " + context.getRetryCount());
        // 실패 조건
        if ("fail".equalsIgnoreCase(message)) {
          throw new RuntimeException(message);
        }
        System.out.println("# 메시지 처리 성공 " + message);
      } catch (Exception e) {
        if (context.getRetryCount() >= 2) {
          rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_TOPIC_DLX,
            RabbitMQConfig.DEAD_LETTER_ROUTING_KEY, message); //= 이 코드가 nack과 같은 기능을 수행
        } else {
          throw e; // 알아서 reject를 보냄
        }
      }
      return null;
    });
  }
}