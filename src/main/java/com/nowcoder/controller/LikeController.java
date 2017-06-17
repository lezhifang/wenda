package com.nowcoder.controller;

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

    @RequestMapping(path = {"/like"}, method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){//commentId指的是comment表的Id号
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);//未登录  返回到登录页面
        }
        Comment comment = commentService.getCommentById(commentId);//这个评论可以是问题的评论  也可以是评论的评论
        long likeCount = likeService.like(hostHolder.getUser().getId(), comment.getEntityType(), comment.getEntityId());//问题的评论
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = RequestMethod.POST)
    @ResponseBody
    public String disLike(@RequestParam("commentId") int commentId){
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);//未登录  返回到登录页面
        }
        Comment comment = commentService.getCommentById(commentId);//这个评论可以是问题的评论  也可以是评论的评论
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), comment.getEntityType(), comment.getEntityId());
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
