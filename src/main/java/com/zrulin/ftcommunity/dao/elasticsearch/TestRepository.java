package com.zrulin.ftcommunity.dao.elasticsearch;

import com.zrulin.ftcommunity.pojo.DiscussPost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zrulin
 * @create 2022-07-09 8:39
 */
@Repository
public interface TestRepository extends CrudRepository<DiscussPost, Integer> {
    Long countByTitle(String lastName);
}
