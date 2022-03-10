package com.zrulin.ftcommunity.dao;

import com.zrulin.ftcommunity.pojo.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-09 16:59
 */
@Mapper
public interface DiscussPostMapper {
    /**
     *
     * @param userId:在sql语句中判断，如果传入参数是0，就按照id查找，如果是非0就按照id查找(动态sql)
     * @param offset:起始行的行号
     * @param limit:每一页最多显示多少行数据
     * @return
     */
    List<DiscussPost> selectDiscussPost(int userId,int offset, int limit);

    /**
     *@Param("userId")  注解的作用是给参数起别名。
     * 如果说在sql里要用到动态的条件，这个条件要用到这个参数，而恰巧这个方法只有一个参数，这个时候这个参数前面一定要起别名，否则会报错。
     * （如果只有一个参数，并且在<if>里使用，则必须加别名）
     * @param userId:在sql语句中判断，如果传入参数是0，就按照id查找，如果是非0就按照id查找(动态sql)
     * @return
     */
    Integer selectDiscussPostRows(@Param("userId") int userId);
}
