package com.example.hellomessagequeue.step7;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

  private final OrderProducer orderProducer;

  public OrderController(OrderProducer orderProducer) {
    this.orderProducer = orderProducer;
  }


  @GetMapping
  public ResponseEntity<String> sendOrderMessage(@RequestParam String message) {
    orderProducer.sendShpping(message);
    return ResponseEntity.ok("Order Completed Message sent: " + message);
  }
}