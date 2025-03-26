package com.jocata.oms.notification.consumer;

//@Service
public class NotificationEventConsumer {

//    private static final Logger logger = LoggerFactory.getLogger(NotificationEventConsumer.class);
//
//    @Autowired
//    private EmailService emailService;

    /*
    @KafkaListener(topics = "order-topic", groupId = "notification-group")
    public void processNotificationEvent(String orderId) {

        System.out.println("Received order event for order id: " + orderId);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("saumyapratap2345@gmail.com");
        emailRequest.setSubject("Order Confirmation");
        emailRequest.setBody("Your order has been confirmed. Order id: " + orderId);
        emailService.sendEmail(emailRequest);
        System.out.println("Email sent successfully!");
    }
     */

//    @KafkaListener(topics = "order-topic", groupId = "notification-group")
//    public void processNotificationEvent(OrderBean orderBean) {
//        logger.info("Received order event for order id: " + orderBean.getOrderId());
//    }
}
