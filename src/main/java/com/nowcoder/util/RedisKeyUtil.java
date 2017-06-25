package com.nowcoder.util;

/**
 * Created by LZF on 2017/6/17.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    //获取粉丝
    private static String BIZ_FOLLOWER = "FOLLOWER";
    //获取关注对象
    private static String BIZ_FOLLOWEE = "FOLLOWEE";

    public static String getLikeKey(int entityType, int entityId){//评论的点赞key
        return BIZ_LIKE + SPLIT + String .valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getFollowerKey(int entityType, int entityId){//某个实体的粉丝(人)
        return BIZ_FOLLOWER + SPLIT + String .valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getFolloweeKey(int userId, int entityType){//人关注某个实体
        return BIZ_FOLLOWEE + SPLIT + String .valueOf(userId) + SPLIT + String.valueOf(entityType);
    }
    public static String getEventqueueKey(){
        return BIZ_EVENTQUEUE;
    }

}
