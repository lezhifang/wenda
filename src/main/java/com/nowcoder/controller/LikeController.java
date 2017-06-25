package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LZF on 2017/6/17.
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    EventProducer eventProducer;
    //可以对题目点赞  也可以对评论点赞  这里演示的是对评论点赞
    @RequestMapping(path = {"/like"}, method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){//commentId指的是comment表的Id号
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);//未登录  返回到登录页面
        }
        Comment comment = commentService.getCommentById(commentId);

        //将相关事件推进异步队列中并返回，然后主线程继续执行点赞操作  而推进异步队列中的事件另外开一个线程进行处理   这就是异步处理   防止卡死
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(comment.getUserId())
                .setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setActorId(hostHolder.getUser().getId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));//这条评论所属的题目Id
        //利用EntityType.ENTITY_COMMENT, commentId(唯一)生成key标识该条评论的赞
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = RequestMethod.POST)
    @ResponseBody
    public String disLike(@RequestParam("commentId") int commentId){
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);//未登录  返回到登录页面
        }
        Comment comment = commentService.getCommentById(commentId);//这个评论可以是问题的评论  也可以是评论的评论
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
