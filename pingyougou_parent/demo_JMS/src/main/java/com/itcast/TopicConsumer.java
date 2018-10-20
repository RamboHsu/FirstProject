package com.itcast;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicConsumer {
    public static void main(String[] args) throws Exception{
        //1.创建连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.135:61616");
        //2.获取连接
        Connection connection = connectionFactory.createConnection();
        //3.启动连接
        connection.start();
        //4.获取session对象(参数1：是否启动事物，参数2：消息确认模式{[AUTO_ACKNOWLEDGE = 1 自动确认],[CLIENT_ACKNOWLEDGE = 2 客户端手动确认],
        //                                                         [DUPS_OK_ACKNOWLEDGE = 3 自动批量确认],[SESSION_TRANSACTED = 0 事务提交并确认]})
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建主题对象
        Topic topic = session.createTopic("test_topic");
        //6.创建消息消费者
        MessageConsumer consumer = session.createConsumer(topic);
        //7.监听消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage)message;
                try{
                    System.out.println("Received Message:"+textMessage.getText());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        //8.等待键盘输入
        System.in.read();
        //9.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
