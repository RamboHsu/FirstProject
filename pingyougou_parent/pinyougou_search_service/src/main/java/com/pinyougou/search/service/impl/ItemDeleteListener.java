package com.pinyougou.search.service.impl;

import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Arrays;

/**
 * 监听：用于删除索引库中记录
 */
@Component
public class ItemDeleteListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        try{
            ObjectMessage objectMessage = (ObjectMessage) message;
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("ItemDeleteListener received message..."+goodsIds);
            itemSearchService.deleteByGoodsId(Arrays.asList(goodsIds));
            System.out.println("Delete IndexDatabase record successfully!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
