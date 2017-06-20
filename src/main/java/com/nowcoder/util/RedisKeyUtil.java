package com.nowcoder.util;

/**
 * Created by LZF on 2017/6/17.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    public static String getLikeKey(int entityType, int entityId){//评论的点赞key
        return BIZ_LIKE + SPLIT + String .valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId){
        return BIZ_DISLIKE + SPLIT + String .valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventqueueKey(){
        return BIZ_EVENTQUEUE;
    }
}
