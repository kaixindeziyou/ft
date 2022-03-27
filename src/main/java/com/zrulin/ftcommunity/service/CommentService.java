package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.dao.CommentMapper;
import com.zrulin.ftcommunity.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-17 8:50
 */
public interface CommentService {

    /**
     * 根据实体和分页搜索帖子
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    public List<Comment> findCommentByEntity(int entityType,int entityId, int offset, int limit);

    /**
     * 根据实体查找帖子数量
     * @param entityType
     * @param entityId
     * @return
     */
    public int findCommentCount(int entityType,int entityId);

    public int addComment(Comment comment);

    public Comment findCommentById(int commentId);
}
