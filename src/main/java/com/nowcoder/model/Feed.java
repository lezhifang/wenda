package com.nowcoder.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created by LZF on 2017/6/25.
 */
public class Feed {//新鲜事  feed流
    private int id;
    private int type;//不同类型  渲染方式不同
    private int userId;
    private Date createdDate;
    //存放JSON格式
    private String data;//存放新鲜事的核心数据
    private JSONObject dataJSON = null;//为了快速的把data里面的内容读取出来

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key){//与ViewObject类似  vo.userName  相当于vo.get(userName);
        return dataJSON == null ? null : dataJSON.getString(key);
    }
    /*
    Velocity中的$obj.xxx
    相当于：obj.getXXX();
    obj.get("XXX");
    obj.isXXX();
     */
}
