package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class PageDeleteListener implements MessageListener{

    @Autowired
    private ItemPageService itemPageService;
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("PageDeleteListener received message:"+goodsIds);
            boolean b = itemPageService.deleteItemHtml(goodsIds);
            System.out.println("Delete the StaticPage?"+b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
