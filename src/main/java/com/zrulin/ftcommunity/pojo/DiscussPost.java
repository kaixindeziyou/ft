package com.zrulin.ftcommunity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-09 16:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "discusspost", shards = 1, replicas = 1)//索引名，类型×，分片，副本。
public class DiscussPost {
    //实体当中的属性和索引中的字段做一个映射。
    @Id
    private Integer id;
    //显示的时候肯定不会显示userId，肯定会显示名称，两种办法：
    //第一种是在写sql语句的时候关联，查询用户，然后去处理，把用户的数据一起查到
    //第二种方式是，得到这个数据以后单独的根据这个数据（DiscussPost），单独的查一下user，然后把查到的user和这个DiscussPost组合在一起返回给页面。
    //采用后者，因为这样的方式看起来好像麻烦点，但是将来使用Redis去缓存一些数据的时候比较方便，到那个时候性能高，代码看起来也直观。
    @Field(type = FieldType.Integer)
    private Integer userId;
    //搜帖子主要就是搜这个标题和内容中间的 关键字
    //分词，存的时候把一句话拆分成更多的字条，增加他搜索的范围，用一个范围非常大的分词器:ik_max_word
    //搜索的时候没必要拆的这么细，用聪明的方式拆出你意思的方式做拆分，拆分的稍微粗一点：ik_smart
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart") //存储时的解析器和搜索时的解析器。
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.Integer)
    private int type;
    @Field(type = FieldType.Integer)
    private int status;
    @Field(type = FieldType.Date)
    private Date createTime;
    @Field(type = FieldType.Integer)
    private int commentCount;
    @Field(type = FieldType.Double)
    private Double score;
}
