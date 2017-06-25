package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LZF on 2017/6/11.
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    FollowService followService;

    @RequestMapping(value="/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        try{
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser() == null){
                //question.setUserId(WendaUtil.ANONYMOUS_USERID);
                return WendaUtil.getJSONString(999);
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question) > 0){//添加问题成功
                return WendaUtil.getJSONString(0);
            }
        }catch(Exception e){
            logger.error("增加问题失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "失败");
    }

    @RequestMapping(value="/question/{qId}", method = {RequestMethod.GET})
    public String questionDetail(Model model,@PathVariable("qId") int qId){
        Question question = questionService.getQuestionById(qId);
        model.addAttribute("question", question);
        //用于展示问题的所有评论
        List<Comment> commentList = commentService.getCommentByEntity(qId, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for(Comment comment : commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            if(hostHolder.getUser() == null){
                vo.set("liked", 0);
            }else{
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user",userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("comments", vos);
        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        //获取关注用户的信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qId, 20);
        for(Integer userId : users){
            ViewObject vo = new ViewObject();
            User user = userService.getUser(userId);
            if(user == null){
                continue;
            }
            vo.set("name", user.getName());
            vo.set("headUrl", user.getHeadUrl());
            vo.set("id", user.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if(hostHolder.getUser() != null){
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qId));
        }else{
            model.addAttribute("followed", false);
        }
        return "detail";
    }
}
