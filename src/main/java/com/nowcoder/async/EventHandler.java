package com.nowcoder.async;

import java.util.List;

/**
 * Created by LZF on 2017/6/20.
 */
public interface EventHandler {
    void doHandler(EventModel model);//处理事件

    List<EventType> getSupportEventTypes();//存放感兴趣的事件类型
}
