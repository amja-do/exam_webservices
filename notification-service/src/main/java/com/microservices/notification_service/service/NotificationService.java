package com.microservices.notification_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void listenOrderEvents(String message) {
        // Simulate sending a notification
        System.out.println("Notification sent: " + message);
    }
}

