package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.dao.DiscussPostMapper;
import com.zrulin.ftcommunity.dao.elasticsearch.DiscussPostRepository;
import com.zrulin.ftcommunity.dao.elasticsearch.TestRepository;
import com.zrulin.ftcommunity.pojo.DiscussPost;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author zrulin
 * @create 2022-07-08 17:31
 */
@SpringBootTest
public class ElasticsearchTest {
    //访问es，数据来源于mysql
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void testInsert(){
        //插入一条数据
//        discussPostRepository.save(discussPostMapper.selectPostDetail(22));
//        discussPostRepository.save(discussPostMapper.selectPostDetail(23));
//        discussPostRepository.save(discussPostMapper.selectPostDetail(24));
        discussPostRepository.save(discussPostMapper.selectPostDetail(25));
    }
    @Test
    public void testInsert1(){
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(0,0,17));
    }

    @Test
    public void updateTest(){
        DiscussPost post = discussPostMapper.selectPostDetail(25);
        post.setTitle("修改第一次");
        discussPostRepository.save(post);
    }

    @Test
    public void deleteTest(){
        discussPostRepository.deleteById(25);
//        discussPostRepository.deleteAll();
    }

    @Test
    public void testSearchByRepository(){
        //查询的条件，我们用的是这样一个接口
        //multiMatchQuery多个字段同时匹配参数：搜索的词条
        //withQuery是构造搜索条件的，整个搜索条件用QueryBuilders去构造
        //构造排序的条件，使用SortBuilders去构造
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()//withQuery还是返回NativeSearchQueryBuilder对象
                .withQuery(QueryBuilders.multiMatchQuery("放假总结","title","content"))
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0,10))//分页
                .withHighlightFields(//指定哪些属性和字段做高亮显示
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        SearchHits<DiscussPost> search = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
        List<DiscussPost> list = new ArrayList<>();
        searchHits.forEach(sh->{
            list.add(sh.getContent());
        });
        System.out.println(list);
    }

    @Test
    public void userTest(){
        long count = testRepository.countByTitle("每日总结");
        System.out.println(count);
    }

    @Test
    public void userTest1(){
        Optional<DiscussPost> byId = testRepository.findById(25);
        System.out.println(byId.get());

    }
}
