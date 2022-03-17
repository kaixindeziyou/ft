package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.dao.CommentMapper;
import com.zrulin.ftcommunity.pojo.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-15 18:08
 */
@SpringBootTest
public class CommentTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void test(){
        for(int i = 0 ; i < 2 ; i++){
            Comment comment = new Comment();
            comment.setUserId(5);
            comment.setEntityId(18);
            comment.setEntityType(2);
            comment.setTargetId(0);
            comment.setContent("祝祖国繁荣昌盛！——2");
            comment.setCreateTime(new Date());
            comment.setStatus(0);
            commentMapper.insertComment(comment);
        }
    }
    @Test
    public void test1(){
        int i = commentMapper.selectCountByEntity(2, 13);
        System.out.println(i);
    }

    @Test
    public void test2(){
        List<Comment> comments = commentMapper.selectCommentsByEntity(2, 13, 0, 3);
        for (Comment comment :comments){
            System.out.println(comment);
        }
    }
}
