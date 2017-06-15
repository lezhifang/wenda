package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by LZF on 2017/6/14.
 */
@Mapper
public interface MessageDAO {
    // 注意字段前后空格
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);//相当于调用message中对应的get方法  比如：from_id = message.getfromId();

    @Update({"update ", TABLE_NAME , " set has_read=1 where id=#{id}"})
    int updatehasRead(@Param("id") int id);

    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME, "where conversation_id=#{conversationId} order by id desc limit #{offset},#{limit} "})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);

    //获取某段会话中收件人有多少条未读信息
    @Select({"select count(id) from", TABLE_NAME, "where has_read=0 and to_id = #{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    /**
     *  #1. SELECT * FROM message WHERE from_id=22 or to_id=22 ORDER BY created_date DESC; #1. 选择跟当前登录用户有关的所有消息(即：当前登录用户既可以为发件人也可以为收件人)并按照创建时间进行降序排序
     *  #2.SELECT * FROM (SELECT * FROM message WHERE from_id=22 or to_id=22 ORDER BY created_date DESC)tt GROUP BY conversation_id;#2.将1中得到的结果按照conversation_id进行分组, 结果:显示每个分组中最新一条消息
     *  #3.将2中得到的结果按照创建时间进行降序排序, 结果:将每个分组中最新一条消息显示出来并按照创建时间排序
     *  (这里使用message的id来存放每个会话中包含消息总数 这里count(id)是统计使用group by后的分组中的个数)
     *  SELECT from_id, to_id, content, has_read, conversation_id, created_date,count(id) as id
     *  FROM (SELECT * FROM message WHERE from_id=22 or to_id=22 ORDER BY created_date DESC)tt GROUP BY conversation_id
     *  ORDER BY created_date DESC LIMIT 0,10;
     */
    @Select({"select ", INSERT_FIELDS,",count(id) as id from (select * from ", TABLE_NAME, "where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
}
