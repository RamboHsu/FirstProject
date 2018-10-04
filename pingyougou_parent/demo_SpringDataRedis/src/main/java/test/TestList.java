package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestList {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void setValue(){
        //左压栈
        redisTemplate.boundListOps("nameList").leftPushAll("Lily","Sam","Jerry");
        //右压栈
        redisTemplate.boundListOps("nameList").rightPush("Tiger");
        redisTemplate.boundListOps("nameList").rightPush("Lily");
    }

    @Test
    public void getValue(){
        List nameList = redisTemplate.boundListOps("nameList").range(0, 10);
        System.out.println(nameList);  //[Jerry, Sam, Lily, Tiger, Lily]
    }

    /**
     * 根据索引查询
     */
    @Test
    public void searchByIndex(){
        String s = (String) redisTemplate.boundListOps("nameList").index(1);
        System.out.println(s);  //Sam
    }

    @Test
    public void removeByIndex(){
        redisTemplate.boundListOps("nameList").remove(1,"Lily");
    }

}
