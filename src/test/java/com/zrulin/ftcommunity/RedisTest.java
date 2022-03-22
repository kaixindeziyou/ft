package com.zrulin.ftcommunity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zrulin
 * @create 2022-03-21 23:38
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void test(){
        String redisTest = "test:count";
        redisTemplate.opsForValue().set(redisTest,1);
        System.out.println(redisTemplate.opsForValue().get(redisTest));
        System.out.println(redisTemplate.opsForValue().increment(redisTest));
        System.out.println(redisTemplate.opsForValue().decrement(redisTest));
    }

    @Test
    public void test1(){
        String key = "test:hash";

        redisTemplate.opsForHash().put(key,"id",1);
        redisTemplate.opsForHash().put(key,"username","张三");
        System.out.println(redisTemplate.opsForHash().get(key,"id"));
        System.out.println(redisTemplate.opsForHash().get(key,"username"));
    }

    @Test
    public void test2(){
        String key = "test:list";
        redisTemplate.opsForList().leftPush(key,101);
        redisTemplate.opsForList().leftPush(key,102);
        redisTemplate.opsForList().leftPush(key,103);

        System.out.println(redisTemplate.opsForList().size(key));
        System.out.println(redisTemplate.opsForList().index(key,0));
        System.out.println(redisTemplate.opsForList().range(key,0,2));

        System.out.println(redisTemplate.opsForList().leftPop(key));
        System.out.println(redisTemplate.opsForList().leftPop(key));
        System.out.println(redisTemplate.opsForList().leftPop(key));
    }

    @Test
    public void test3(){
        String key = "test:set";
        redisTemplate.opsForSet().add(key,"刘备","关羽","张飞","赵云","黄忠","马超");

        System.out.println(redisTemplate.opsForSet().size(key));
        System.out.println(redisTemplate.opsForSet().pop(key));
        System.out.println(redisTemplate.opsForSet().members(key));
    }

    @Test
    public void test4(){
        String key = "test:zset";
        redisTemplate.opsForZSet().add(key,"唐僧",80);
        redisTemplate.opsForZSet().add(key,"孙悟空",70);
        redisTemplate.opsForZSet().add(key,"猪八戒",60);
        redisTemplate.opsForZSet().add(key,"沙和尚",50);
        redisTemplate.opsForZSet().add(key,"白龙马",40);

        System.out.println(redisTemplate.opsForZSet().zCard(key));
        System.out.println(redisTemplate.opsForZSet().count(key,35,60));
        System.out.println(redisTemplate.opsForZSet().range(key,0,3));
        System.out.println(redisTemplate.opsForZSet().reverseRange(key,0,3));
        System.out.println(redisTemplate.opsForZSet().rank(key,"沙和尚"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(key,"沙和尚"));
        System.out.println(redisTemplate.opsForZSet().remove(key,"唐僧"));
        System.out.println(redisTemplate.opsForZSet().score(key,"孙悟空"));
        System.out.println(redisTemplate.opsForZSet().range(key,0,3));
    }

    @Test
    public void test5(){
//        redisTemplate.delete("test:users");
//        System.out.println(redisTemplate.hasKey("test:zset"));
        System.out.println(redisTemplate.keys("*"));

        System.out.println(redisTemplate.expire("test:set",10, TimeUnit.SECONDS));
    }

    //多次访问同一个key，用绑定的方式来简化
    @Test
    public void testBoundOperations(){
        String key = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(key);
        System.out.println(operations.get());
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

    //在开发的时候spring也是支持编程式事务，还有声明式事务，声明式事务更简单，只要做一些配置，加一个注解就可以了。
    //声明式事务只能精确到一个方法，方法的内部整个逻辑都是事务的范围，方法之内就没法去查询了，所以通常用编程式事务
    //编程式事务
    @Test
    public  void testTransaction(){
        Object obj = redisTemplate.execute(new SessionCallback() {
            //这个sessionCallback接口里面带了一个execute方法，redisTemplate.execute调用的时候，它底层自己去调的。
            //调的时候它会把这个 operations（执行命令的对象）传过来，用这个对象来执行命令，管理事务。
            //最终这个方法会返回一些数据，返回给这个redisTemplate.execute方法。
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String key = "test:tx";

                operations.multi();//启动事务
                operations.opsForSet().add(key,"啥也不是");
                operations.opsForSet().add(key,"好像也是");
                operations.opsForSet().add(key,"当然不是");

                //因为在这个里面事务还没有提交，所以查询是没有结果的。
                System.out.println(operations.opsForSet().members(key));

                return operations.exec();//提交事务
            }
        });
        //结果：[1, 1, 1, [好像也是, 当然不是, 啥也不是]]
        // 每一个1表示每一次影响的数据的行数
        System.out.println(obj);
    }
}
