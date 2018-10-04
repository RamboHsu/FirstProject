package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestHash {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void setHashValue(){
        redisTemplate.boundHashOps("nameHash").put(1,"a");
        redisTemplate.boundHashOps("nameHash").put(2,"b");
        redisTemplate.boundHashOps("nameHash").put(3,"c");
        redisTemplate.boundHashOps("nameHash").put(4,"d");
    }

    @Test
    public void getKeys(){
        Set keys = redisTemplate.boundHashOps("nameHash").keys();
        System.out.println(keys);
    }

    @Test
    public void getValues(){
        List values = redisTemplate.boundHashOps("nameHash").values();
        System.out.println(values);
    }

    @Test
    public void getValueByKey(){
        String s = (String) redisTemplate.boundHashOps("nameHash").get(1);
        System.out.println(s);
    }

    @Test
    public void removeByKey(){
        redisTemplate.boundHashOps("nameHash").delete(1,2);
    }
}
