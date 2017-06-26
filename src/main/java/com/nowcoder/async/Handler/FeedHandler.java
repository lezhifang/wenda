package com.nowcoder.async.Handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by LZF on 2017/6/20.
 */
@Component
public class FeedHandler implements EventHandler{
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    FeedService feedService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;

    private String buildFeedData(EventModel model){//存放新鲜事的核心数据
        Map<String, String> map = new HashMap<String, String>();
        //触发用户是通用的
        User actor = userService.getUser(model.getActorId());
        if(actor == null){
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", String.valueOf(actor.getHeadUrl()));
        map.put("userName", String.valueOf(actor.getName()));

        //(评论问题或者关注问题)新鲜事
        if(model.getEventType() == EventType.COMMENT ||
                (model.getEventType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)){
            Question question = questionService.getQuestionById(model.getEntityId());
            if(question == null){
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        if(model.getEventType() == EventType.LIKE){
            Comment comment = commentService.getCommentById(model.getEntityId());
            if(comment == null){
                return null;
            }

            map.put("commentId", String.valueOf(comment.getId()));
            map.put("questionId", String.valueOf(comment.getEntityId()));
            Question question = questionService.getQuestionById(comment.getEntityId());
            if(question == null){
                return null;
            }
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return  null;
    }
    @Override
    public void doHandler(EventModel model) {
        //为了测试，把model的ueserId随机一下
//        Random random = new Random();
//        model.setActorId(1 + random.nextInt(10));

        //构造一个feed流
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getEventType().getValue());
        feed.setData(buildFeedData(model));
        if(feed.getData() == null){
            return ;
        }
        feedService.addFeed(feed);

        //获取所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        //系统队列
        followers.add(0);//0是系统  当没有登录的时候只能看系统的队列
        //给所有粉丝推事件
        for(int follower : followers){
            String timelineKey = RedisKeyUtil.getTimeLineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
            //限制最大长度，如果timelineKey的List长度过大，就删除后面的新鲜事
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW, EventType.LIKE});
    }
}
