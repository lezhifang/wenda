package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by LZF on 2017/6/14.
 */
@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private SensitiveService sensitiveService;

    public int addComment(Comment comment){
        //敏感词过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentDAO.selectCommentByEntity(entityId, entityType);
    }

   public void deleteComment(int entityId, int entityType){
        commentDAO.updateStatus(1, entityId, entityType);//1表示失效
   }

   public int getCommentCount(int entityId, int entityType){
       return commentDAO.getCommentCount(entityId, entityType);
   }

}
