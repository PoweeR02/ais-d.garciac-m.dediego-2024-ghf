package es.codeurjc.ais.nitflex.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void notify(String message) {
        logger.info(message);
    }
}

