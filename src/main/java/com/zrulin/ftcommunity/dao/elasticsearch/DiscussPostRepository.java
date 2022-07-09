package com.zrulin.ftcommunity.dao.elasticsearch;

import com.zrulin.ftcommunity.pojo.DiscussPost;
import org.elasticsearch.client.ElasticsearchClient;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 数据访问层的代码,es可以看作是一个特殊的数据库
 * @author zrulin
 * @create 2022-07-08 17:27
 */
@Repository //Repository是spring针对数据访问层的注解<实体类类型，实体类中的主键类型>
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
