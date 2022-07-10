package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.elasticsearch.DiscussPostRepository;
import com.zrulin.ftcommunity.pojo.DiscussPost;
import com.zrulin.ftcommunity.service.ElasticsearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-07-09 15:04
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void saveDiscussPost(DiscussPost discussPost) {
        discussPostRepository.save(discussPost);
    }

    @Override
    public void deleteDiscussPost(int id) {
        discussPostRepository.deleteById(id);
    }

    @Override
    public Map<String ,Object> searchDiscussPost(String keyword, int current, int limit) {
        Map<String, Object> map = new HashMap<>();
        //查询的条件，我们用的是这样一个接口
        //multiMatchQuery多个字段同时匹配参数：搜索的词条
        //withQuery是构造搜索条件的，整个搜索条件用QueryBuilders去构造
        //构造排序的条件，使用SortBuilders去构造
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()//withQuery还是返回NativeSearchQueryBuilder对象
                .withQuery(QueryBuilders.multiMatchQuery(keyword,"title","content"))
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current,limit))//分页
                .withHighlightFields(//指定哪些属性和字段做高亮显示
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        SearchHits<DiscussPost> search = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);
        System.out.println(search.getTotalHits());

        map.put("totalHits",search.getTotalHits());
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
        List<DiscussPost> list = new ArrayList<>();
        searchHits.forEach(sh->{
            list.add(sh.getContent());
        });
        map.put("posts",list);
        return map;
    }
}
