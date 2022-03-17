package com.zrulin.ftcommunity.dao;

import com.zrulin.ftcommunity.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-15 17:51
 */
@Mapper
public interface CommentMapper {

    /**
     * 根据实体来查询帖子，并且分页。
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    public List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit);

    /**
     * 根据实体类型和id查询帖子数量
     * @param entityType
     * @param entityId
     * @return
     */
    public Integer selectCountByEntity(int entityType,int entityId);

    /**
     * 新添评论
     * @param comment
     * @return
     */
    public int insertComment(Comment comment);
}
