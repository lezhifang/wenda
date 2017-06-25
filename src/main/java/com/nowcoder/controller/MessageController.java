package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LZF on 2017/6/14.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @RequestMapping(value="/msg/detail", method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId){
        try{
            if(hostHolder.getUser() == null){
                return "redirect:/reglogin";
            }
            List<Message> conversationDetailList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for(Message message : conversationDetailList){
                //添加查看已读 (若该消息未读且当前登录用户为收件人，在显示消息详情时标记已读)
                if(message.getHasRead() == 0 && hostHolder.getUser().getId() == message.getToId()){
                    messageService.updatehasRead(message.getId());
                }
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch(Exception e){
            logger.error("获取详情消息失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(value="/msg/list", method = {RequestMethod.GET})
    public String getConversationList(Model model){
        try{
            if(hostHolder.getUser() == null){
                return "redirect:/reglogin";
            }
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0 ,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for(Message message : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                //如果当前登录用户等于发件人,则显示收件人的相关信息; 否则显示发件人  (即:站内信列表需要看到对方信息)
                int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
                vo.set("user", userService.getUser(targetId));
                vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
                messages.add(vo);
            }
            model.addAttribute("conversations",messages);
        }catch(Exception e){
            logger.error("获取消息列表失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(value="/msg/addMessage", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("toName") String toName,
                              @RequestParam("content") String content){
        try{
            if(hostHolder.getUser() == null){
                return WendaUtil.getJSONString(999, "未登录");
            }
            User user = userService.selectByName(toName);
            if(user == null){
                return WendaUtil.getJSONString(1, "用户不存在");
            }

            if(hostHolder.getUser().getId() == user.getId()){
                return WendaUtil.getJSONString(1, "无法发送消息给自己");
            }

            Message message = new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);//由于发送消息对应的是弹窗，所以返回一个JSON
        }catch(Exception e){
            logger.error("发送消息失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发送信息失败");
        }
    }
}
