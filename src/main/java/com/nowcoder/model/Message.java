package com.nowcoder.model;

import java.util.Date;

/**
 * Created by LZF on 2017/6/14.
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createdDate;
    private int hasRead;
    private String conversationId;//A发送给B  与B发送给A  属于同一个会话

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getConversationId() {
        //小的放前 大的放后  用于显示当前登录用户与某个用户的所有聊天信息
        if(fromId < toId){
            return String.format("%d_%d", fromId,toId);
        }else{
            return String.format("%d_%d", toId,fromId);
        }
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
