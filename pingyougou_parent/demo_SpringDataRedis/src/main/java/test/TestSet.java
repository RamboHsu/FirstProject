package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestSet {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue(){
        redisTemplate.boundSetOps("nameSet").add("Lily","Sam","Jerry");
        redisTemplate.boundSetOps("nameSet").add("Jack");
    }

    @Test
    public void getValue(){
        Set set = redisTemplate.boundSetOps("nameSet").members();
        System.out.println(set);
    }

    @Test
    public void removeValue(){
        redisTemplate.boundSetOps("nameSet").remove("Sam","Jack");
    }

    @Test
    public void delete(){
        redisTemplate.delete("nameSet");

    }
}
