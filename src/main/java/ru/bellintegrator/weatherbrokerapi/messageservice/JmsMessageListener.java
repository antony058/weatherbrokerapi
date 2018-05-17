package ru.bellintegrator.weatherbrokerapi.messageservice;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.bellintegrator.weatherbrokerapi.weather.service.WeatherService;
import ru.bellintegrator.weatherbroker.server.weather.view.WeatherView;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

//@Component
public class JmsMessageListener {
    private Logger log = LoggerFactory.getLogger(JmsMessageListener.class);

    @Autowired
    private WeatherService weatherService;

    @Value("${jms.destination.topic.jndi}")
    private String destination;

    @Transactional(rollbackFor = Exception.class)
//    @JmsListener(destination = "java:jboss/exported/jms/topic/messageBoxTopic")
    public void gotMessage(Message message) throws JMSException, NotFoundException {
        if (message instanceof ObjectMessage) {
            WeatherView view = (WeatherView) ((ObjectMessage) message).getObject();

            log.info("Данные о погоде получены из topic");
            ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView weatherView =
                    new ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView(
                            view.getCity(), view.getDate(),
                            view.getTemp(), view.getText());

            weatherService.save(weatherView);
        }
    }
}
