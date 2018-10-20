package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

@Component
public class ItemSearchListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;


    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Listener received messages:"+textMessage);
            String text = textMessage.getText();
            List<TbItem> list = JSON.parseArray(text, TbItem.class);
            for (TbItem item : list) {
                System.out.println(item.getGoodsId() + " " + item.getTitle());
                Map specMap = JSON.parseObject(item.getSpec());//将spec中的json字符串转换成Map
                item.setSpecMap(specMap);//给带注解的字段赋值
            }
            itemSearchService.importList(list);//导入到solr索引库
            System.out.println("Import to Solr IndexDatabase successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
