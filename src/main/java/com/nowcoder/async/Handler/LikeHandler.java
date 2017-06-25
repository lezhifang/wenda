package com.nowcoder.async.Handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by LZF on 2017/6/20.
 */
@Component
public class LikeHandler implements EventHandler{
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel model) {//发送站内信通知被点赞用户
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);//管理员发送的站内信
        message.setToId(model.getEntityOwnerId());
        message.setHasRead(0);
        message.setCreatedDate(new Date());
        User user= userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的评论，http://127.0.0.1:8080/question/" + model.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
