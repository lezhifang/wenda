package com.nowcoder.dao;

import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by LZF on 2017/5/30.
 */
@Mapper
public interface QuestionDAO {
    // 注意空格
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, comment_count,created_date,user_id ";

    //使用注解配置
//    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
//            ") values (#{title},#{content},#{commentCount},#{createdDate},#{userId})"})
//    int addQuestion(Question question);

    //使用xml配置方式，自动将传进去的参数Question对象中的属性值赋值给xml中的相应变量
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

}
