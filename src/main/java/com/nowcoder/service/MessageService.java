package com.nowcoder.service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by LZF on 2017/6/14.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private SensitiveService sensitiveService;


    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        //敏感词过滤
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message);
    }

    public int updatehasRead(int id){
        return  messageDAO.updatehasRead(id);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    //获取某段会话中收件人有多少条未读信息
    public int getConversationUnreadCount(int userId, String conversationId){
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }
}
