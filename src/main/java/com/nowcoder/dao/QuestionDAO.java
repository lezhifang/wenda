package com.nowcoder.dao;

import com.nowcoder.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by LZF on 2017/5/30.
 */
@Mapper
public interface QuestionDAO {
    // 注意空格
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, comment_count,created_date,user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //使用注解配置
//    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
//            ") values (#{title},#{content},#{commentCount},#{createdDate},#{userId})"})
//    int addQuestion(Question question);

    //使用xml配置方式，自动将传进去的参数Question对象中的属性值赋值给xml中的相应变量
    int addQuestion(Question question);//添加成功返回1

    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    Question selectQuestionById(int id);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Update({"update ", TABLE_NAME , " set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);
}
