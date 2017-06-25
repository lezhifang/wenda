package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * Created by LZF on 2017/6/16.
 */
@Component
public class JedisAdapter implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key, value);//添加成功返回1  否则返回0
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key, value);//删除成功返回1  否则返回0
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);//统计元素总个数
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismenmber(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key, value);//存在返回true  不存在返回false
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    public long lpush(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    /*
    * 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长(单位是秒)。 反之，返回一个含有两个元素的列表，第一个元素
    * 是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
    * 补充：当超时时间为"0"，表示不限制等待的时间，即如果没有新元素加入列表就会永远阻塞下去。
    */
    public List<String> brpop(int timeout, String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis){
        try{
            return jedis.multi();
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }
        return null;
    }

    public long zadd(String key, double score, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long zrem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrem(key, value);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public List<Object> exec(Transaction tx, Jedis jedis){
        try{
            return tx.exec();//Exec 命令用于执行所有事务块内的命令  返回：事务块内所有命令的返回值，按命令执行的先后顺序排列。 当操作被打断时，返回空值 nil
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
            tx.discard();//使用discard可以rollback
        }finally {
           if (tx != null){
               try {
                   tx.close();
               } catch (IOException e) {
                   logger.error("发生异常" + e.getMessage());
               }
           }
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrange(String key, int start, int end){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrevrange(String key, int start, int end){//从大到小排列
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key){//从大到小排列
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zcard(key);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member){//从大到小排列
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public List<String> lrange(String key, int start, int end){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    private static void print(int index, Object obj){
        System.out.println(String.format("%d. %s", index, obj.toString()));
    }
    public static void main(String[] args){
        //如果new Jedis()没参数,表明连接默认的本地端口6379。也可以指定，这里9表示第9个数据库，默认有16个 索引为0~15
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();//在测试时清空这个数据库  若为flushAll()表示清空所有的数据库

        //KV：单一数值，验证码，PV，缓存
        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello", "newHello");
        print(2,jedis.get("newHello"));
        jedis.setex("hello2", 15, "world2");//表示设置"hello2"的value值的有效期为15秒
        //进行简单加减法操作  比如记录浏览量  如果用一个表来存 每次访问改写数据需锁定这个表  导致容易卡死
        jedis.set("pv", "100");//需要是整数，否则报错：ERR value is not an integer or out of range
        jedis.incr("pv");//自增1
        print(3,jedis.get("pv"));
        jedis.incrBy("pv", 5);//原基础上增加5
        print(4,jedis.get("pv"));
        jedis.decrBy("pv",3);//原基础上减少3
        print(5,jedis.get("pv"));
        print(6,jedis.keys("*"));

        //List：双向列表，适用于最新列表，关注列表
        String listName = "list";
        //若原本存在key为"list" 那么在测试时需要jedis.del("list");
        for(int i = 0; i < 10; i++){
            jedis.lpush(listName,"a" + String.valueOf(i));
        }
        print(7,jedis.lrange(listName, 0, 10));
        print(8,jedis.lrange(listName, 0 ,3));
        print(9, jedis.lpop(listName));
        print(10, jedis.llen(listName));
        jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "ll");
        jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "fang");
        print(11, jedis.lrange(listName, 0, 12));//可以用来显示最新列表 结果：[a8, a7, a6, a5, fang, a4, ll, a3, a2, a1, a0]
        //假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
        List<String> list= jedis.brpop(0, listName);//"list"和a0  Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止

        //Hash：对象属性，不定长属性数
        String userKey = "userXXX";
        jedis.hset(userKey, "name", "lzf");
        jedis.hset(userKey, "age", "22");
        jedis.hset(userKey, "photo", "15016421231");
        print(12,jedis.hget(userKey, "name"));
        print(13, jedis.hgetAll(userKey));//获取key为userKey的所有HashMap中的key-value的值结果：{name=lzf, photo=15016421231, age=22}
        jedis.hdel(userKey,"photo");
        print(14, jedis.hexists(userKey, "photo"));
        print(15, jedis.hexists(userKey, "name"));
        print(16, jedis.hkeys(userKey));//获取key为userKey的所有HashMap中的key键值
        print(17, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "school", "shanshida");
        jedis.hsetnx(userKey, "name", "fangfang");//若果不存在，则设置，否则不设置  结果;{name=lzf, school=shanshida, age=22}
        print(18, jedis.hgetAll(userKey));

        //Set(无序集合)：适用于无顺序的集合(去重)，点赞点踩，抽奖，已读，共同好友
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for(int  i = 0; i < 10; i++){
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * i));
        }
        print(19, jedis.smembers(likeKey1));//结果;[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
        print(20, jedis.smembers(likeKey2));//结果：[0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
        print(21, jedis.sunion(likeKey1, likeKey2));//b并集结果：[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 16, 25, 36, 49, 64, 81]
        print(22, jedis.sdiff(likeKey1, likeKey2));//属于likeKey1但不属于likeKey2  结果;[8, 2, 3, 5, 6, 7]
        print(23, jedis.sinter(likeKey1, likeKey2));//求交集
        print(24, jedis.sismember(likeKey1, "8"));
        jedis.srem(likeKey1, "8");//删除
        print(25, jedis.sismember(likeKey1, "8"));
        jedis.smove(likeKey2, likeKey1, "80");//从likeKey2中移"81"到likeKey1中  如果likeKey2不存在"81"，则likeKey1不变
        print(27, jedis.smembers(likeKey1));
        print(28, jedis.scard(likeKey1));//查看这个集合有多少个元素
        print(29, jedis.srandmember(likeKey1,3));//进行抽奖  在likeKey1中随机取3个

        //SortedSet(排序集合)：排行榜，优先队列  (相对于Set, SortedSet中的每个元素带了一个权重score)
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "Jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 75, "Lucy");
        jedis.zadd(rankKey, 80, "Mei");
        print(30, jedis.zcard(rankKey));
        print(31, jedis.zcount(rankKey, 61, 100));
        print(32, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Lucy");
        print(33, jedis.zscore(rankKey, "Lucy"));//结果为 77.0
        jedis.zincrby(rankKey, 2, "Luc");
        print(34, jedis.zscore(rankKey, "Luc"));//2.0
        print(35, jedis.zrange(rankKey, 0, 100));//[Luc, Jim, Ben, Lucy, Mei, Lee]  从小到大排列
        print(36, jedis.zrange(rankKey, 1, 3));//[Jim, Ben, Lucy]
        print(37, jedis.zrevrange(rankKey, 1, 3));//[Mei, Lucy, Ben]  从大到小排列
        /**
         * 进行遍历 结果如下：
         38. Ben:60.0
         38. Lucy:77.0
         38. Mei:80.0
         38. Lee:90.0
         */
        for(Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")){
            print(38, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        print(39, jedis.zrank(rankKey, "Ben"));// 2(索引号) 因为默认已经从小到达排序好
        print(40, jedis.zrevrank(rankKey, "Ben"));//逆序排  3

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        print(41, jedis.zlexcount(setKey, "-", "+"));//计算有序集合中指定字典区间内成员数量
        print(42, jedis.zlexcount(setKey, "(b", "[d"));//2
        print(43, jedis.zlexcount(setKey, "[b", "[d"));//3
        jedis.zrem(setKey, "b");
        print(44, jedis.zrange(setKey, 0, 10));//[a, c, d, e]
        jedis.zremrangeByLex(setKey, "(c", "+");
        print(45, jedis.zrange(setKey, 0, 10));//[a, c]

        //连接池  默认有8个连接
        JedisPool jedisPool = new JedisPool();
        for(int  i = 0; i < 16; i++){
            Jedis j = jedisPool.getResource();
            print(46, j.get("pv"));
            j.close();//每次用完需要释放
        }

        //使用redis做缓存
        User user = new User();
        user.setHeadUrl("a.png");
        user.setSalt("salt");
        user.setPassword("ppp");
        user.setName("shoushou");
        user.setId(1);
        //序列化JSONObject.toJSONString(user)和反序列化JSON.parseObject(jedis.get("user1"), User.class)
        print(47, JSONObject.toJSONString(user));//{"headUrl":"a.png","id":1,"name":"shoushou","password":"ppp","salt":"salt"}
        jedis.set("user1", JSONObject.toJSONString(user));
        print(48, JSON.parseObject(jedis.get("user1"), User.class));


    }
}



