package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LZF on 2017/5/30.
 * ViewObject不是视图，是用来传递对象和设置Velocity之间用的一个中间对象(例如：vo.question 这里相当于vo调用get(question)方法)
 *(提示：当一个表中用到其他表中的字段，可以考虑使用这种方式，例如：question中字段userId是user表中的字段id)
 */
public class ViewObject {
    private Map<String,Object> objs = new HashMap<String,Object>();
    public void set(String key, Object value){
        objs.put(key, value);
    }
    public Object get(String key){
        return objs.get(key);
    }
}
