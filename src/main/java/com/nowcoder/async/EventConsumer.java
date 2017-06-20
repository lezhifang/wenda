package com.nowcoder.async;
import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;

import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LZF on 2017/6/20.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    //存放EventType相关联的所有Handler  即：对EventType事件类型感兴趣的所有Handler   需要对其进行初始化
    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null){
            for(Map.Entry<String, EventHandler> entry : beans.entrySet()){//常用的两种遍历方式entrySet()和keySet() 推荐使用entrySet()
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();//获取该EventHandler感兴趣的所有事件类型

                for(EventType type : eventTypes){//遍历该事件类型并在config中添加(key -> EventType)和相应的(value -> Handler)
                    if(!config.containsKey(type)){
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }//完成config初始化工作

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key = RedisKeyUtil.getEventqueueKey();
                    List<String> eventInfo = jedisAdapter.brpop(0, key);
                    for(String message : eventInfo){
                        if(message.equals(key)){//过滤掉返回的key
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if(!config.containsKey(eventModel.getEventType())){
                            logger.error("不能识别的事件！！");
                            continue;
                        }
                        for(EventHandler handler : config.get(eventModel.getEventType())){
                            //处理对该事件类型感兴趣的所有EventHandler
                            handler.doHandler(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}










