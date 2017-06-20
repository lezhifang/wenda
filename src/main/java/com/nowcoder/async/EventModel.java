package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LZF on 2017/6/20.
 * 放入单向队列中的事件
 */
public class EventModel {
    private EventType eventType;
    private int actorId;//发起该事件的Id
    private int entityType;
    private int entityId;
    private int eventOwnerId;

    private Map<String, String> exts = new HashMap<String, String>();//扩展字段  用来存放事件发生时需要保存的一些信息

    public EventModel(){

    }

    public EventModel(EventType eventType){
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEventOwnerId() {
        return eventOwnerId;
    }

    public EventModel setEventOwnerId(int eventOwnerId) {
        this.eventOwnerId = eventOwnerId;
        return this;
    }

    public String getExt(String key) {//依据key返回value
        return exts.get(key);
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}