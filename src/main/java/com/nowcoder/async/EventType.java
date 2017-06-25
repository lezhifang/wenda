package com.nowcoder.async;

/**
 * Created by LZF on 2017/6/20.
 * enum学习参考网址：http://www.cnblogs.com/hyl8218/p/5088287.html
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MALL(3),
    FOLLOW(4),
    UNFOLLOW(5);

    private int value;
    EventType(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
