package com.nowcoder.controller;

import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by LZF on 2017/6/14.
 */
@Controller
public class CommentController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    //增加问题中的评论
    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            if(hostHolder.getUser() != null){
                comment.setUserId(hostHolder.getUser().getId());
            }else{
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setStatus(0);
            commentService.addComment(comment);
            //更新question表中的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),count);
        }catch(Exception e){
            logger.error("增加评论失败！" + e.getMessage());
        }
        return "redirect:/question/" + questionId;
    }
}
