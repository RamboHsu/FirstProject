package cn.itcast.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class QueueController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @RequestMapping("/send")
    public void send(String text){
        jmsMessagingTemplate.convertAndSend("itcast",text);
    }

    @RequestMapping("/sendSms")
    public void sendSms(){
        Map map = new HashMap();
        map.put("mobile","17623226066");
        map.put("template_code", "SMS_148866436");
        map.put("sign_name", "品优购");
        map.put("param", "{\"name\":\"Rambo\", \"number\":\"123456\"}");

        jmsMessagingTemplate.convertAndSend("sms",map);
    }

}
