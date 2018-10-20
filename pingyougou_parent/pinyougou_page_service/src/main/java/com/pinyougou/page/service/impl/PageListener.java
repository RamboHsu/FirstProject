package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听类(用于生成静态页)
 */
@Component
public class PageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            final String text = textMessage.getText();
            System.out.println("Received message :" + text);
            boolean b = itemPageService.genItemHtml(Long.parseLong(text));
            System.out.println("Produced the StaticPage yet?--" + b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
