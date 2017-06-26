package com.nowcoder.service;

import com.nowcoder.dao.FeedDAO;
import com.nowcoder.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * Created by LZF on 2017/6/25.
 */
@Service
public class FeedService {

    @Autowired
    FeedDAO feedDAO;

    public boolean addFeed(Feed feed){
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed selectFeedById(int id){
        return feedDAO.selectFeedById(id);
    }

    //选择关注用户的新鲜事
    public List<Feed> selectUserFeeds(int maxId, List<Integer> userIds, int count){
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }
}