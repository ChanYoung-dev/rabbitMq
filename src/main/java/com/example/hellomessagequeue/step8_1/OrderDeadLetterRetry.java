package com.example.hellomessagequeue.step8_1;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 데드레터로 들어온 메시지를 Requeue 한다.
 *
 * @author : codevillain
 * @fileName : OrderDeadLetterRetry
 * @since : 12/26/24
 */
@Component
public class OrderDeadLetterRetry {

  private final RabbitTemplate rabbitTemplate;

  public OrderDeadLetterRetry(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @RabbitListener(queues = RabbitMQConfig.DLQ)
  public void processDlqMessage(String failedMessage) {
    try {
      // "fail" 메시지를 수정하여 성공적으로 처리되도록 변경
      if ("fail".equalsIgnoreCase(failedMessage)) {
        failedMessage = "success";
        System.out.println("[DLQ] Message fixed: " + failedMessage);
      } else {
        // 이미 수정된 메시지는 다시 처리하지 않음
        System.err.println("[DLQ] Message already fixed. Ignoring: " + failedMessage);
        return;
      }

      // 수정된 메시지를 원래 큐로 재전송
      rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_TOPIC_EXCHANGE,
        "order.completed.shipping",
        failedMessage);

      System.out.println("Message successfully reprocessed : " + failedMessage);

    } catch (Exception e) {
      System.err.println("Error processing DLQ message : " + e);
    }
  }
}